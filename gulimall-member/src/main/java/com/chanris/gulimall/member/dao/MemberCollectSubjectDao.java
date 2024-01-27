package com.chanris.gulimall.member.dao;

import com.chanris.gulimall.common.dao.BaseDao;
import com.chanris.gulimall.member.entity.MemberCollectSubjectEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员收藏的专题活动
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Mapper
public interface MemberCollectSubjectDao extends BaseDao<MemberCollectSubjectEntity> {
	
}