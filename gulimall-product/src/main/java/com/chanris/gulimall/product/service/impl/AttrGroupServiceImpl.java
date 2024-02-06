package com.chanris.gulimall.product.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.product.dao.AttrGroupDao;
import com.chanris.gulimall.product.dto.AttrGroupDTO;
import com.chanris.gulimall.product.entity.AttrGroupEntity;
import com.chanris.gulimall.product.service.AttrGroupService;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 属性分组
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Slf4j
@Service
public class AttrGroupServiceImpl extends CrudServiceImpl<AttrGroupDao, AttrGroupEntity, AttrGroupDTO> implements AttrGroupService {

    /**
     * AttrGroupServiceImpl调用的page 的所有QueryWrapper查询条件定义在这里
     * @param params
     * @return
     */
    @Override
    public QueryWrapper<AttrGroupEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");
        String attrGroupName = (String)params.get("attrGroupName");

        Object cid = params.get("catelogId");
        Long catelogId = null;
        if (cid instanceof String && ! cid.equals("")) {
            catelogId = Long.parseLong((String) cid);
        }else if(cid instanceof Number) {
            catelogId = (Long) cid;
        }
        log.info("通用 page 查询， QueryWrapper: attrGroupName: {}", attrGroupName);
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        // wrapper.eq(boolean condition, string column, object value);
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);
        wrapper.like(StrUtil.isNotBlank(attrGroupName), "attr_group_name", attrGroupName);
        wrapper.eq(ObjectUtil.isNotNull(catelogId), "catelog_id", catelogId);
        return wrapper;
    }


}