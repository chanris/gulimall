package com.chanris.gulimall.product.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.common.utils.ObjectConvert;
import com.chanris.gulimall.product.dao.SkuInfoDao;
import com.chanris.gulimall.product.dto.SkuInfoDTO;
import com.chanris.gulimall.product.dto.SpuInfoDescDTO;
import com.chanris.gulimall.product.entity.SkuImagesEntity;
import com.chanris.gulimall.product.entity.SkuInfoEntity;
import com.chanris.gulimall.product.entity.SpuInfoDescEntity;
import com.chanris.gulimall.product.service.*;
import cn.hutool.core.util.StrUtil;
import com.chanris.gulimall.product.vo.SkuItemSaleAttrVo;
import com.chanris.gulimall.product.vo.SkuItemVo;
import com.chanris.gulimall.product.vo.SpuItemAttrGroupVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * sku信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service("skuInfoService")
public class SkuInfoServiceImpl extends CrudServiceImpl<SkuInfoDao, SkuInfoEntity, SkuInfoDTO> implements SkuInfoService {

    @Resource
    private SkuInfoDao skuInfoDao;
    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Resource
    private SpuInfoDescService spuInfoDescService;
    @Resource
    private AttrGroupService attrGroupService;
    @Resource
    private SkuImagesService skuImagesService;
    @Resource
    private ThreadPoolExecutor executor;

    @Override
    public QueryWrapper<SkuInfoEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");
        Long catalogId = ObjectConvert.toLong(params.get("catalogId"));
        Long brandId = ObjectConvert.toLong(params.get("brandId"));
        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);
        wrapper.eq(ObjectUtil.isNotNull(catalogId), "catalog_id", catalogId);
        wrapper.eq(ObjectUtil.isNotNull(brandId), "brand_id", brandId);
        return wrapper;
    }

    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {
        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spu_id", spuId);
        return skuInfoDao.selectList(queryWrapper);
    }

    @Override
    public SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException {

        SkuItemVo skuItemVo = new SkuItemVo();

        CompletableFuture<SkuInfoEntity> infoFuture = CompletableFuture.supplyAsync(() -> {
            //1、sku基本信息的获取  pms_sku_info
            SkuInfoDTO infoDTO = this.get(skuId);
            SkuInfoEntity infoEntity = new SkuInfoEntity();
            BeanUtils.copyProperties(infoDTO, infoEntity);
            skuItemVo.setInfo(infoEntity);
            return infoEntity;
        }, executor);

        // thenAcceptAsync 当上一个任务成功时，异步执行
        CompletableFuture<Void> saleAttrFuture = infoFuture.thenAcceptAsync((res) -> {
            //3、获取spu的销售属性组合
            List<SkuItemSaleAttrVo> saleAttrVos = skuSaleAttrValueService.getSaleAttrBySpuId(res.getSpuId());
            skuItemVo.setSaleAttr(saleAttrVos);
        }, executor);


        CompletableFuture<Void> descFuture = infoFuture.thenAcceptAsync((res) -> {
            //4、获取spu的介绍    pms_spu_info_desc
            SpuInfoDescDTO spuInfoDescDTO =  spuInfoDescService.get(res.getSpuId());
            SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
            BeanUtils.copyProperties(spuInfoDescDTO, spuInfoDescEntity);
            skuItemVo.setDesc(spuInfoDescEntity);
        }, executor);


        CompletableFuture<Void> baseAttrFuture = infoFuture.thenAcceptAsync((res) -> {
            //5、获取spu的规格参数信息
            List<SpuItemAttrGroupVo> attrGroupVos = attrGroupService.getAttrGroupWithAttrsBySpuId(res.getSpuId(), res.getCatalogId());
            skuItemVo.setGroupAttrs(attrGroupVos);
        }, executor);


        // Long spuId = info.getSpuId();
        // Long catalogId = info.getCatalogId();

        //2、sku的图片信息    pms_sku_images
        CompletableFuture<Void> imageFuture = CompletableFuture.runAsync(() -> {
            List<SkuImagesEntity> imagesEntities = skuImagesService.getImagesBySkuId(skuId);
            skuItemVo.setImages(imagesEntities);
        }, executor);

/*
        CompletableFuture<Void> seckillFuture = CompletableFuture.runAsync(() -> {
            //3、远程调用查询当前sku是否参与秒杀优惠活动
            R skuSeckilInfo = seckillFeignService.getSkuSeckilInfo(skuId);
            if (skuSeckilInfo.getCode() == 0) {
                //查询成功
                SeckillSkuVo seckilInfoData = skuSeckilInfo.getData("data", new TypeReference<SeckillSkuVo>() {
                });
                skuItemVo.setSeckillSkuVo(seckilInfoData);

                if (seckilInfoData != null) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime > seckilInfoData.getEndTime()) {
                        skuItemVo.setSeckillSkuVo(null);
                    }
                }
            }
        }, executor);
*/

        //等到所有任务都完成
        CompletableFuture.allOf(saleAttrFuture,descFuture,baseAttrFuture,imageFuture).get();

        return skuItemVo;
    }
}