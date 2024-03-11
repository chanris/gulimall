package com.chanris.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.product.dao.SkuSaleAttrValueDao;
import com.chanris.gulimall.product.dto.SkuSaleAttrValueDTO;
import com.chanris.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.chanris.gulimall.product.service.SkuSaleAttrValueService;
import cn.hutool.core.util.StrUtil;
import com.chanris.gulimall.product.vo.SkuItemSaleAttrVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class SkuSaleAttrValueServiceImpl extends CrudServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity, SkuSaleAttrValueDTO> implements SkuSaleAttrValueService {

    @Resource
    private SkuSaleAttrValueDao skuSaleAttrValueDao;

    @Override
    public QueryWrapper<SkuSaleAttrValueEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SkuSaleAttrValueEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public List<SkuItemSaleAttrVo> getSaleAttrBySpuId(Long spuId) {

        List<SkuItemSaleAttrVo> saleAttrVos = skuSaleAttrValueDao.getSaleAttrBySpuId(spuId);

        return saleAttrVos;
    }

    /**
     * 根据skuId 获得商品销售属性 的StringList 格式数据
     * @param skuId
     * @return
     */
    @Override
    public List<String> getSkuSaleAttrValuesAsStringList(Long skuId) {
        return skuSaleAttrValueDao.getSkuSaleAttrValuesAsStringList(skuId);
    }
}