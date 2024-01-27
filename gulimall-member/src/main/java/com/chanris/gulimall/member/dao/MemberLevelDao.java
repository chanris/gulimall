package com.chanris.gulimall.member.dao;

import com.chanris.gulimall.common.dao.BaseDao;
import com.chanris.gulimall.member.entity.MemberLevelEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员等级
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Mapper
public interface MemberLevelDao extends BaseDao<MemberLevelEntity> {
	
}