package com.chanris.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.product.dao.SpuInfoDescDao;
import com.chanris.gulimall.product.dto.SpuInfoDescDTO;
import com.chanris.gulimall.product.entity.SpuInfoDescEntity;
import com.chanris.gulimall.product.service.SpuInfoDescService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * spu信息介绍
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class SpuInfoDescServiceImpl extends CrudServiceImpl<SpuInfoDescDao, SpuInfoDescEntity, SpuInfoDescDTO> implements SpuInfoDescService {

    @Override
    public QueryWrapper<SpuInfoDescEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SpuInfoDescEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}