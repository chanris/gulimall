package com.chanris.gulimall.order.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.order.dao.OrderReturnApplyDao;
import com.chanris.gulimall.order.dto.OrderReturnApplyDTO;
import com.chanris.gulimall.order.entity.OrderReturnApplyEntity;
import com.chanris.gulimall.order.service.OrderReturnApplyService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 订单退货申请
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class OrderReturnApplyServiceImpl extends CrudServiceImpl<OrderReturnApplyDao, OrderReturnApplyEntity, OrderReturnApplyDTO> implements OrderReturnApplyService {

    @Override
    public QueryWrapper<OrderReturnApplyEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<OrderReturnApplyEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}