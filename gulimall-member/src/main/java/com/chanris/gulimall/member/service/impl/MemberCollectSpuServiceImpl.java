package com.chanris.gulimall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.member.service.MemberCollectSpuService;
import com.chanris.gulimall.member.dao.MemberCollectSpuDao;
import com.chanris.gulimall.member.dto.MemberCollectSpuDTO;
import com.chanris.gulimall.member.entity.MemberCollectSpuEntity;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 会员收藏的商品
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class MemberCollectSpuServiceImpl extends CrudServiceImpl<MemberCollectSpuDao, MemberCollectSpuEntity, MemberCollectSpuDTO> implements MemberCollectSpuService {

    @Override
    public QueryWrapper<MemberCollectSpuEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<MemberCollectSpuEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}