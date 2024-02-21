package com.chanris.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.common.to.SkuReductionTo;
import com.chanris.gulimall.common.to.SpuBoundTo;
import com.chanris.gulimall.common.utils.Result;
import com.chanris.gulimall.product.dao.SpuInfoDao;
import com.chanris.gulimall.product.dto.*;
import com.chanris.gulimall.product.entity.SkuImagesEntity;
import com.chanris.gulimall.product.entity.*;
import com.chanris.gulimall.product.feign.CouponFeignService;
import com.chanris.gulimall.product.service.*;
import cn.hutool.core.util.StrUtil;
import com.chanris.gulimall.product.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * spu信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Slf4j
@Service
public class SpuInfoServiceImpl extends CrudServiceImpl<SpuInfoDao, SpuInfoEntity, SpuInfoDTO> implements SpuInfoService {

    @Resource
    private SpuImagesService spuImagesService;
    @Resource
    private SpuInfoDescService spuInfoDescService;
    @Resource
    private AttrService attrService;
    @Resource
    private ProductAttrValueService productAttrValueService;
    @Resource
    private CouponFeignService couponFeignService;
    @Resource
    private SkuInfoService skuInfoService;
    @Resource
    private SkuImagesService skuImagesService;
    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Override
    public QueryWrapper<SpuInfoEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * 新增商品
     * @param vo
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void savesupInfo(SpuSaveVo vo) {
        // 保存spu基本信息: pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo, spuInfoEntity);
        this.saveBaseSpuInfo(spuInfoEntity);

        //保存spu的描述图片: pms_spu_info_desc
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",",decript));
        spuInfoDescService.saveSpuInfoDesc(spuInfoDescEntity);

        //保存spu的图片集: pms_spu_images
        List<String> images = vo.getImages();
        spuImagesService.saveImages(spuInfoEntity.getId(), images);

        //保存spu的规格参数: pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
            valueEntity.setAttrId(attr.getAttrId());

            //查询attr属性名
            AttrDTO byId = attrService.get(attr.getAttrId());

            valueEntity.setAttrName(byId.getAttrName());
            valueEntity.setAttrValue(attr.getAttrValues());
            valueEntity.setQuickShow(attr.getShowDesc());
            valueEntity.setSpuId(spuInfoEntity.getId());

            return valueEntity;
        }).toList();
        productAttrValueService.insertBatch(collect);

        //保存spu的积分信息: sms_spu_bounds
        Bounds bounds = vo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds, spuBoundTo);
        spuBoundTo.setSpuId(spuInfoEntity.getId());
        Result r = couponFeignService.saveSpuBounds(spuBoundTo);

        if(r.getCode() != 0) {
            log.error("远程保存spu积分信息失败");
        }

        //保存当前spu对应的所有sku信息: pms_sku_info
        // sku的基本西悉尼: pms:sku_info
        List<Skus> skus = vo.getSkus();
        if(skus != null && skus.size() > 0) {
            skus.forEach(item -> {
                String defaultImg = "";
                for(Images image: item.getImages()) {
                    if(image.getDefaultImg() == 1) {
                        defaultImg = image.getImgUrl();
                    }
                }

                SkuInfoDTO skuInfoDTO = new SkuInfoDTO();
                BeanUtils.copyProperties(item, skuInfoDTO);
                skuInfoDTO.setBrandId(spuInfoEntity.getBrandId());
                skuInfoDTO.setCatalogId(skuInfoDTO.getCatalogId());
                skuInfoDTO.setSaleCount(0L);
                skuInfoDTO.setSpuId(spuInfoEntity.getId());
                skuInfoDTO.setSkuDefaultImg(defaultImg);
                skuInfoService.save(skuInfoDTO);

                Long skuId = skuInfoDTO.getSkuId();
                List<SkuImagesEntity> skuImagesEntities = item.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    return skuImagesEntity;
                }).filter(dto -> !StringUtils.hasLength(dto.getImgUrl())).toList();

                //5.2 sku的图片信息: pms_sku_images
                skuImagesService.insertBatch(skuImagesEntities);
                // 5.3 sku的销售属性: pms_sku_sale_attr_value
                List<Attr> attrs = item.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attrs.stream().map(a -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuId);
                    return skuSaleAttrValueEntity;
                }).toList();

                skuSaleAttrValueService.insertBatch(skuSaleAttrValueEntities);

                //5.4 sku的优惠、减免等信息： sms_sku_ladder
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item, skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if(skuReductionTo.getFullCount() > 0 || skuReductionTo.getFullPrice().compareTo(BigDecimal.ZERO) > 0) {
                    Result r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if(!r1.success()) {
                        log.error("远程保存sku积分信息失败");
                    }
                }
            });
        }
    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseDao.insert(spuInfoEntity);
    }
}