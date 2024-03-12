package com.chanris.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.ware.dao.WareSkuDao;
import com.chanris.gulimall.ware.dto.WareSkuDTO;
import com.chanris.gulimall.ware.entity.WareSkuEntity;
import com.chanris.gulimall.ware.exception.NoStockException;
import com.chanris.gulimall.ware.service.WareSkuService;
import cn.hutool.core.util.StrUtil;
import com.chanris.gulimall.ware.vo.OrderItemVo;
import com.chanris.gulimall.ware.vo.SkuHasStockVo;
import com.chanris.gulimall.ware.vo.WareSkuLockVo;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class WareSkuServiceImpl extends CrudServiceImpl<WareSkuDao, WareSkuEntity, WareSkuDTO> implements WareSkuService {

    @Resource
    private WareSkuDao wareSkuDao;

    @Override
    public QueryWrapper<WareSkuEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public WareSkuEntity getByWrapper(QueryWrapper<WareSkuEntity> wrapper) {
        return wareSkuDao.selectOne(wrapper);
    }

    @Override
    public List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds) {
//        skuIds.stream().map(sku -> {
//            SkuHasStockVo vo = new SkuHasStockVo();
//           //select sum(stock - stoc k_locked) from wms_ware_sku where sku_id = 1
//            //查询当前sku的总库存量
//            return vo;
//        })

        return wareSkuDao.getSkuHasStock(skuIds);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public boolean orderLockStock(WareSkuLockVo vo) {
        // 1.找到商品在哪个仓库有库存
        List<OrderItemVo> locks = vo.getLocks();
        List<SkuWareHasStock> hasStocks = locks.stream().map(item -> {
            SkuWareHasStock stock = new SkuWareHasStock();
            Long skuId = item.getSkuId();
            List<Long> ids = wareSkuDao.listWareIdHasSkuStock(skuId);
            stock.setWareId(ids);
            stock.setNum(item.getCount());
            stock.setSkuId(skuId);
            return stock;
        }).toList();

        boolean allLock = true;
        // 锁定库存
        for (SkuWareHasStock hasStock : hasStocks) {
            boolean skuStocked = false;
            Long skuId = hasStock.getSkuId();
            List<Long> wareIds = hasStock.getWareId();
            if(wareIds == null || wareIds.size() == 0) {
                throw new NoStockException("库存不足 skuId:" + skuId);
            }
            for(Long wareId: wareIds) {
                long count = wareSkuDao.lockSkuStock(skuId, wareId, hasStock.getNum());
                if(count == 1L) {
                    skuStocked = true;
                    break;
                }
            }
            if(!skuStocked) {
                throw new NoStockException("商品没有库存 skuId:" + skuId);
            }
        }
        return true;
    }

    @Data
    class SkuWareHasStock {
        private Long skuId;
        private List<Long> wareId;
        private Integer num;
    }
}