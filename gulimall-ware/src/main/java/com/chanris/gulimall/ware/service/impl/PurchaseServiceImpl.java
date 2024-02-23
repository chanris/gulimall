package com.chanris.gulimall.ware.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.chanris.gulimall.common.constant.WareConstant;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.common.utils.ObjectConvert;
import com.chanris.gulimall.ware.dao.PurchaseDao;
import com.chanris.gulimall.ware.dao.PurchaseDetailDao;
import com.chanris.gulimall.ware.dao.WareSkuDao;
import com.chanris.gulimall.ware.dto.PurchaseDTO;
import com.chanris.gulimall.ware.dto.PurchaseDetailDTO;
import com.chanris.gulimall.ware.entity.PurchaseDetailEntity;
import com.chanris.gulimall.ware.entity.PurchaseEntity;
import com.chanris.gulimall.ware.entity.WareSkuEntity;
import com.chanris.gulimall.ware.feign.ProductFeignService;
import com.chanris.gulimall.ware.service.PurchaseDetailService;
import com.chanris.gulimall.ware.service.PurchaseService;
import cn.hutool.core.util.StrUtil;
import com.chanris.gulimall.ware.service.WareInfoService;
import com.chanris.gulimall.ware.service.WareSkuService;
import com.chanris.gulimall.ware.vo.MergeVo;
import com.chanris.gulimall.ware.vo.PurchaseDoneVo;
import com.chanris.gulimall.ware.vo.PurchaseItemDoneVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class PurchaseServiceImpl extends CrudServiceImpl<PurchaseDao, PurchaseEntity, PurchaseDTO> implements PurchaseService {

    @Resource
    private PurchaseDao purchaseDao;
    @Resource
    private PurchaseDetailDao purchaseDetailDao;
    @Resource
    private PurchaseDetailService purchaseDetailService;
    @Resource
    private WareSkuService wareSkuService;
    @Resource
    private WareSkuDao wareSkuDao;
    @Resource
    private ProductFeignService productFeignService;

    @Override
    public QueryWrapper<PurchaseEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");
        Integer status = ObjectConvert.toInteger(params.get("status"));

        QueryWrapper<PurchaseEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);
        wrapper.eq(ObjectUtil.isNotNull(status), "status", status);


        return wrapper;
    }

    @Override
    public List<PurchaseDTO> getUnreceiveList() {
        return purchaseDao.unreceivePurchaseList();
    }

    @Override
    public void mergePurchaseDetail(MergeVo vo) {
        purchaseDetailDao.merge(vo);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void receiveByUserId(List<Long> purchaseIds, Long userId) {
        //1.获得需要修改的purchase、purchase_detail
        QueryWrapper<PurchaseEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("assignee_id", userId);
        wrapper.in("id", purchaseIds);

        List<PurchaseEntity> purchaseEntities = purchaseDao.selectList(wrapper)
                .stream()
                .peek(item -> item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode()))
                .toList();
        // 根据主键更新
        this.updateBatchById(purchaseEntities);

        List<Long> validPurchaseIds =  purchaseEntities.stream().map(PurchaseEntity::getId).toList();;
        if(validPurchaseIds.size() == 0) return;
        QueryWrapper<PurchaseDetailEntity> detailWrapper = new QueryWrapper<>();
        detailWrapper.in("purchase_id", validPurchaseIds);
        List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailDao.selectList(detailWrapper)
                .stream()
                .peek(item-> item.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode()))
                .toList();
        purchaseDetailService.updateBatchById(purchaseDetailEntities);
    }

    /**
     * 知识点!: mybatis-plus update 操作默认 不会更新 null字段，
     * 即如果传入的entity或vo的某个字段为 null，那么它不会更新该字段。
     *
     * @param doneVo
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void done(PurchaseDoneVo doneVo) {
        // 1.改变采购项状态
        boolean hasError = false; // 采购单中有采购项为失败状态， 则采购单状态为 HASERROR
        List<PurchaseDetailEntity> purchaseEntities = new ArrayList<>(doneVo.getItems().size());
        for (PurchaseItemDoneVo item : doneVo.getItems()) {
            PurchaseDetailEntity entity = new PurchaseDetailEntity();
            entity.setId(item.getItemId());
            entity.setStatus(item.getStatus());
            if(entity.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()) {
                hasError = true;
            }else {
                // 采购项完成，入库
//                wareSkuService.addStock()
            }
            purchaseEntities.add(entity);
        }
        purchaseDetailService.updateBatchById(purchaseEntities);

        // 2. 通过updateWrapper 改变采购单状态
        UpdateWrapper<PurchaseEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", doneVo.getId());
        updateWrapper.set("status", hasError ? WareConstant.PurchaseStatusEnum.HASERROR.getCode() : WareConstant.PurchaseStatusEnum.FINISH.getCode());
        this.update(null, updateWrapper);

        // 3.将成功采购的进行入库
        // 获得成功的采购项
        List<Long> storageIds = purchaseEntities.stream()
                .filter(item -> item.getStatus() == WareConstant.PurchaseDetailStatusEnum.FINISH.getCode())
                .map(PurchaseDetailEntity::getId).toList();
        Map<String, Object> condition = new HashMap<>(1);
        condition.put("storageIds", storageIds);
        List<PurchaseDetailDTO> validDetailDTOs = purchaseDetailService.list(condition);
        for (PurchaseDetailDTO detailDTO : validDetailDTOs) {
            WareSkuEntity skuEntity = new WareSkuEntity();
            skuEntity.setSkuId(detailDTO.getSkuId());
            skuEntity.setStock(detailDTO.getSkuNum());
            skuEntity.setWareId(detailDTO.getWareId());

            // todo 23/2/24
            String skuName = productFeignService.get(detailDTO.getSkuId()).getData().getSkuName();
            skuEntity.setSkuName(skuName);
            // 锁定库存
            skuEntity.setStockLocked(0);

            // 根据 skuId 和 仓库id 判断是否存在 库存
            QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("sku_id", detailDTO.getSkuId());
            wrapper.eq("ware_id", detailDTO.getWareId());
            WareSkuEntity dbSkuEntity = wareSkuDao.selectOne(wrapper);
            if(dbSkuEntity != null) {
                dbSkuEntity.setStock(skuEntity.getStock() + dbSkuEntity.getStock());
                wareSkuService.updateById(dbSkuEntity);
            }else {
                wareSkuService.insert(skuEntity);
            }
        }
    }
}