package com.chanris.gulimall.order.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.order.dao.OrderOperateHistoryDao;
import com.chanris.gulimall.order.dto.OrderOperateHistoryDTO;
import com.chanris.gulimall.order.entity.OrderOperateHistoryEntity;
import com.chanris.gulimall.order.service.OrderOperateHistoryService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 订单操作历史记录
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class OrderOperateHistoryServiceImpl extends CrudServiceImpl<OrderOperateHistoryDao, OrderOperateHistoryEntity, OrderOperateHistoryDTO> implements OrderOperateHistoryService {

    @Override
    public QueryWrapper<OrderOperateHistoryEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<OrderOperateHistoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}