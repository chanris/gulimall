package com.chanris.gulimall.product.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.chanris.gulimall.common.constant.ProductConstant;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.common.to.SkuHasStockVo;
import com.chanris.gulimall.common.to.SkuReductionTo;
import com.chanris.gulimall.common.to.SpuBoundTo;
import com.chanris.gulimall.common.to.es.SkuEsModel;
import com.chanris.gulimall.common.utils.ObjectConvert;
import com.chanris.gulimall.common.utils.Result;
import com.chanris.gulimall.product.dao.SpuInfoDao;
import com.chanris.gulimall.product.dto.*;
import com.chanris.gulimall.product.entity.SkuImagesEntity;
import com.chanris.gulimall.product.entity.*;
import com.chanris.gulimall.product.feign.CouponFeignService;
import com.chanris.gulimall.product.feign.SearchFeignService;
import com.chanris.gulimall.product.feign.WareFeignService;
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
import java.util.*;
import java.util.stream.Collectors;

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
    @Resource
    private BrandService brandService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private WareFeignService wareFeignService;
    @Resource
    private SearchFeignService searchFeignService;
    @Resource
    private SpuInfoDao spuInfoDao;

    @Override
    public QueryWrapper<SpuInfoEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");
        Long catalogId = ObjectConvert.toLong(params.get("catalogId"));
        Long brandId = ObjectConvert.toLong(params.get("brandId"));
        Integer publishStatus = ObjectConvert.toInteger(params.get("publishStatus"));
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);
        wrapper.eq(ObjectUtil.isNotNull(catalogId), "catalog_id", catalogId);
        wrapper.eq(ObjectUtil.isNotNull(brandId), "brand_id", brandId);
        wrapper.eq(ObjectUtil.isNotNull(publishStatus), "publish_status", publishStatus);

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
                skuInfoDTO.setCatalogId(spuInfoEntity.getCatalogId());

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
                }).filter(dto -> StringUtils.hasLength(dto.getImgUrl())).toList();

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
                // Spring BeanUtils.copyProperties 不会copy的 List类型的属性
//                skuReductionTo.setMemberPrice(item.getMemberPrice());
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

    /**
     * 商品上架
     * @param spuId
     */
    @Override
    public void up(Long spuId) {

        // 组装需要的数据
        // 1.查出当前spuId对应的商品信息
        List<SkuInfoEntity> skus = skuInfoService.getSkusBySpuId(spuId);
        List<Long> skuIds = skus.stream().map(SkuInfoEntity::getSkuId).toList();

        // TODO 27/2/24 查看当前sku的所有规格属性
        List<ProductAttrValueEntity> baseAttrs = productAttrValueService.baseAttrListForSpuId(spuId);
        List<Long> attrIds = baseAttrs.stream().map(ProductAttrValueEntity::getAttrId).toList();
        List<Long> searchAttrIds = attrService.selectSearchAttrs(attrIds);
        Set<Long> validAttrIds = new HashSet<>(searchAttrIds);
        List<SkuEsModel.Attrs> attrList = baseAttrs
                .stream()
                .filter(item -> validAttrIds.contains(item.getAttrId()))
                .map(item -> {
            SkuEsModel.Attrs attrs1 = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item, attrs1);
            return attrs1;
        }).toList();
        Map<Long, Boolean> stockMap = null;
        try {
            Result<List<SkuHasStockVo>> skusHasStock = wareFeignService.getSkusHasStock(skuIds);
            if (skusHasStock.success()) {
                List<SkuHasStockVo> data = skusHasStock.getData();
                stockMap = data.stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, SkuHasStockVo::getHasStock));
            }
        }catch (Exception e) {
            log.warn("远程获得是否有库存失败" + e.getMessage());
        }

        /**
         * Java为了保证在lambda表达式中的外部作用域中的变量，不会被外部作用域中修改，要求lambda表达式中的变量都为effective final
         * 这样做的好处是，使得代码更加健壮和可预测，避免在并发或异步编程中可能导致的竞态条件或不确定性。
         */
        // 2.封装每个sku的信息
        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModel> upProducts = skus.stream().map(sku -> {
            SkuEsModel esModel = new SkuEsModel();
            BeanUtils.copyProperties(sku, esModel);
            // skuPrice,skuImg
            esModel.setSkuPrice(sku.getPrice());
            esModel.setSkuImg(sku.getSkuDefaultImg());

            // hasStock,hotScore
            // TODO 27/2/24 远程调用，库存系统查询是否有库存 默认有库存
            esModel.setHasStock(finalStockMap != null ? finalStockMap.get(sku.getSkuId()) : true);

            // TODO 27/2/24 热度评分，默认 0
            esModel.setHotScore(0L);

            // TODO 27/2/24 查看品牌和分类的名称
            // 循环查库了 大忌！！！有待优化
            BrandDTO brandDTO = brandService.get(sku.getBrandId());
            esModel.setBrandName(brandDTO.getName());
            esModel.setBrandImg(brandDTO.getLogo());
            CategoryDTO categoryDTO = categoryService.get(sku.getCatalogId());
            esModel.setCatalogName(categoryDTO.getName());

            // 设置检索属性
            esModel.setAttrs(attrList);
            return esModel;
        }).toList();

        // TODO 27/2/24 将数据发送给es进行保存
        Result<?> r = searchFeignService.productStatusUp(upProducts);
        if(r.success()) {
            // 上架成功 修改spu的状态
            UpdateWrapper<SpuInfoEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", spuId);
            updateWrapper.set("publish_status", ProductConstant.StateEnum.SPU_UP.code);
            spuInfoDao.update(null, updateWrapper);
        }else {
            // TODO 重复调用？ 接口幂等性； 重试机制
            log.error("远程调用es失败");

            /**
             * feign的调用流程
             * 1. 构造请求数据，将对象转为json
             *    RequestTemplate template = buildTemplateFormArgs.create(argv)
             * 2. 发生请求进行执行
             *    executeAndDecode(template)
             * 3. 执行请求会有重试机制
             *
             */
        }
    }
}