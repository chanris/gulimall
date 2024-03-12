package com.chanris.gulimall.order.vo;

import com.chanris.gulimall.order.entity.OrderEntity;
import lombok.Data;

/**
 * @author chenyue7@foxmail.com
 * @date 12/3/2024
 * @description
 */
@Data
public class SubmitOrderRespVo {
    private OrderEntity order;
    private Integer code; // 0:成功,其他失败
}
