package com.chanris.gulimall.member.dao;

import com.chanris.gulimall.common.dao.BaseDao;
import com.chanris.gulimall.member.entity.IntegrationChangeHistoryEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 积分变化历史记录
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Mapper
public interface IntegrationChangeHistoryDao extends BaseDao<IntegrationChangeHistoryEntity> {
	
}