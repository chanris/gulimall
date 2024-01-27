package com.chanris.gulimall.order.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.order.dao.OrderSettingDao;
import com.chanris.gulimall.order.dto.OrderSettingDTO;
import com.chanris.gulimall.order.entity.OrderSettingEntity;
import com.chanris.gulimall.order.service.OrderSettingService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 订单配置信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class OrderSettingServiceImpl extends CrudServiceImpl<OrderSettingDao, OrderSettingEntity, OrderSettingDTO> implements OrderSettingService {

    @Override
    public QueryWrapper<OrderSettingEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<OrderSettingEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}