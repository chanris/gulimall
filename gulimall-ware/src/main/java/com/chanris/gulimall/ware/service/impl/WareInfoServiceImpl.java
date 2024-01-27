package com.chanris.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.ware.dao.WareInfoDao;
import com.chanris.gulimall.ware.dto.WareInfoDTO;
import com.chanris.gulimall.ware.entity.WareInfoEntity;
import com.chanris.gulimall.ware.service.WareInfoService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class WareInfoServiceImpl extends CrudServiceImpl<WareInfoDao, WareInfoEntity, WareInfoDTO> implements WareInfoService {

    @Override
    public QueryWrapper<WareInfoEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<WareInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}