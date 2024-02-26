package com.chanris.gulimall.product.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.common.utils.ConvertUtils;
import com.chanris.gulimall.common.utils.ObjectConvert;
import com.chanris.gulimall.product.dao.ProductAttrValueDao;
import com.chanris.gulimall.product.dto.ProductAttrValueDTO;
import com.chanris.gulimall.product.entity.ProductAttrValueEntity;
import com.chanris.gulimall.product.service.ProductAttrValueService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class ProductAttrValueServiceImpl extends CrudServiceImpl<ProductAttrValueDao, ProductAttrValueEntity, ProductAttrValueDTO> implements ProductAttrValueService {

    @Resource
    private ProductAttrValueDao productAttrValueDao;

    @Override
    public QueryWrapper<ProductAttrValueEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");
        Long spuId = ObjectConvert.toLong(params.get("spuId"));
        QueryWrapper<ProductAttrValueEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);
        wrapper.eq(ObjectUtil.isNotNull(spuId), "spu_id", spuId);
        return wrapper;
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void updateSpuAttr(Long spuId, List<ProductAttrValueDTO> dtos) {
        // 1.删除这个spuId之前对应的所有规格属性
        QueryWrapper<ProductAttrValueEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spu_id", spuId);
        productAttrValueDao.delete( queryWrapper);
        dtos.forEach(item-> item.setSpuId(spuId));
        List<ProductAttrValueEntity> productAttrValueEntities = ConvertUtils.sourceToTarget(dtos, ProductAttrValueEntity.class);
        this.insertBatch(productAttrValueEntities);
    }
}