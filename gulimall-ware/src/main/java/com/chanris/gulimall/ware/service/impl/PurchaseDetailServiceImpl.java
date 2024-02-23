package com.chanris.gulimall.ware.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.common.utils.ObjectConvert;
import com.chanris.gulimall.ware.dao.PurchaseDetailDao;
import com.chanris.gulimall.ware.dto.PurchaseDetailDTO;
import com.chanris.gulimall.ware.entity.PurchaseDetailEntity;
import com.chanris.gulimall.ware.service.PurchaseDetailService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class PurchaseDetailServiceImpl extends CrudServiceImpl<PurchaseDetailDao, PurchaseDetailEntity, PurchaseDetailDTO> implements PurchaseDetailService {

    @Override
    public QueryWrapper<PurchaseDetailEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");
        Long wareId = ObjectConvert.toLong(params.get("wareId"));
        Integer status = ObjectConvert.toInteger(params.get("status"));
        List<Long> storageIds = (List<Long>) params.get("storageIds");
        QueryWrapper<PurchaseDetailEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);
        wrapper.eq(ObjectUtil.isNotNull(wareId), "ware_id", wareId);
        wrapper.eq(ObjectUtil.isNotNull(status), "status", status);
        wrapper.in(ObjectUtil.isNotNull(storageIds), "id", storageIds);
        return wrapper;
    }


}