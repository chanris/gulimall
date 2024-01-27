package com.chanris.gulimall.order.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.order.dao.PaymentInfoDao;
import com.chanris.gulimall.order.dto.PaymentInfoDTO;
import com.chanris.gulimall.order.entity.PaymentInfoEntity;
import com.chanris.gulimall.order.service.PaymentInfoService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 支付信息表
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class PaymentInfoServiceImpl extends CrudServiceImpl<PaymentInfoDao, PaymentInfoEntity, PaymentInfoDTO> implements PaymentInfoService {

    @Override
    public QueryWrapper<PaymentInfoEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<PaymentInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}