package com.chanris.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.ware.dao.WareOrderTaskDao;
import com.chanris.gulimall.ware.dto.WareOrderTaskDTO;
import com.chanris.gulimall.ware.entity.WareOrderTaskEntity;
import com.chanris.gulimall.ware.service.WareOrderTaskService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class WareOrderTaskServiceImpl extends CrudServiceImpl<WareOrderTaskDao, WareOrderTaskEntity, WareOrderTaskDTO> implements WareOrderTaskService {

    @Override
    public QueryWrapper<WareOrderTaskEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<WareOrderTaskEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}