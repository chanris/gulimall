package com.chanris.gulimall.order.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.order.dao.OrderReturnReasonDao;
import com.chanris.gulimall.order.dto.OrderReturnReasonDTO;
import com.chanris.gulimall.order.entity.OrderReturnReasonEntity;
import com.chanris.gulimall.order.service.OrderReturnReasonService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 退货原因
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class OrderReturnReasonServiceImpl extends CrudServiceImpl<OrderReturnReasonDao, OrderReturnReasonEntity, OrderReturnReasonDTO> implements OrderReturnReasonService {

    @Override
    public QueryWrapper<OrderReturnReasonEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<OrderReturnReasonEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}