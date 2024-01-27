package com.chanris.gulimall.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * 会员统计信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
@ApiModel(value = "会员统计信息")
public class MemberStatisticsInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "会员id")
	private Long memberId;

	@ApiModelProperty(value = "累计消费金额")
	private BigDecimal consumeAmount;

	@ApiModelProperty(value = "累计优惠金额")
	private BigDecimal couponAmount;

	@ApiModelProperty(value = "订单数量")
	private Integer orderCount;

	@ApiModelProperty(value = "优惠券数量")
	private Integer couponCount;

	@ApiModelProperty(value = "评价数")
	private Integer commentCount;

	@ApiModelProperty(value = "退货数量")
	private Integer returnOrderCount;

	@ApiModelProperty(value = "登录次数")
	private Integer loginCount;

	@ApiModelProperty(value = "关注数量")
	private Integer attendCount;

	@ApiModelProperty(value = "粉丝数量")
	private Integer fansCount;

	@ApiModelProperty(value = "收藏的商品数量")
	private Integer collectProductCount;

	@ApiModelProperty(value = "收藏的专题活动数量")
	private Integer collectSubjectCount;

	@ApiModelProperty(value = "收藏的评论数量")
	private Integer collectCommentCount;

	@ApiModelProperty(value = "邀请的朋友数量")
	private Integer inviteFriendCount;


}