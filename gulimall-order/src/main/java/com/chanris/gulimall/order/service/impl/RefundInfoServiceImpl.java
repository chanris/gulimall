package com.chanris.gulimall.order.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.order.dao.RefundInfoDao;
import com.chanris.gulimall.order.dto.RefundInfoDTO;
import com.chanris.gulimall.order.entity.RefundInfoEntity;
import com.chanris.gulimall.order.service.RefundInfoService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 退款信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class RefundInfoServiceImpl extends CrudServiceImpl<RefundInfoDao, RefundInfoEntity, RefundInfoDTO> implements RefundInfoService {

    @Override
    public QueryWrapper<RefundInfoEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<RefundInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}