package com.chanris.gulimall.ware.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.common.utils.ObjectConvert;
import com.chanris.gulimall.ware.dao.WareOrderTaskDetailDao;
import com.chanris.gulimall.ware.dto.WareOrderTaskDetailDTO;
import com.chanris.gulimall.ware.entity.WareOrderTaskDetailEntity;
import com.chanris.gulimall.ware.service.WareOrderTaskDetailService;
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
public class WareOrderTaskDetailServiceImpl extends CrudServiceImpl<WareOrderTaskDetailDao, WareOrderTaskDetailEntity, WareOrderTaskDetailDTO> implements WareOrderTaskDetailService {

    @Override
    public QueryWrapper<WareOrderTaskDetailEntity> getWrapper(Map<String, Object> params){
        Object id = params.get("id");
        Object taskId = params.get("task_id");
        Object lockStatus = params.get("lock_status");

        QueryWrapper<WareOrderTaskDetailEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(ObjectUtil.isNotNull(id), "id", ObjectConvert.toLong(id));
        wrapper.eq(ObjectUtil.isNotNull(taskId), "task_id", ObjectConvert.toLong(taskId));
        wrapper.eq(ObjectUtil.isNotNull(lockStatus), "lock_status", ObjectConvert.toInteger(lockStatus));
        return wrapper;
    }


}