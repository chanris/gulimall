package com.chanris.gulimall.product.dao;

import com.chanris.gulimall.common.dao.BaseDao;
import com.chanris.gulimall.product.entity.CommentReplayEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品评价回复关系
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Mapper
public interface CommentReplayDao extends BaseDao<CommentReplayEntity> {
	
}