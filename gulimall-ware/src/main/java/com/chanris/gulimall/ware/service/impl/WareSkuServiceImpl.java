package com.chanris.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.ware.dao.WareSkuDao;
import com.chanris.gulimall.ware.dto.WareSkuDTO;
import com.chanris.gulimall.ware.entity.WareSkuEntity;
import com.chanris.gulimall.ware.service.WareSkuService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    public QueryWrapper<WareSkuEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public WareSkuEntity getByWrapper(QueryWrapper<WareSkuEntity> wrapper) {
        WareSkuEntity skuEntity = wareSkuDao.selectOne(wrapper);
        return skuEntity;
    }
}