package com.chanris.gulimall.coupon.dao;

import com.chanris.gulimall.common.dao.BaseDao;
import com.chanris.gulimall.coupon.entity.CouponHistoryEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券领取历史记录
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Mapper
public interface CouponHistoryDao extends BaseDao<CouponHistoryEntity> {
	
}