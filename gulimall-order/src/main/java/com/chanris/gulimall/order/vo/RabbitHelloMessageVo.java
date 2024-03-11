package com.chanris.gulimall.order.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chenyue7@foxmail.com
 * @date 11/3/2024
 * @description
 */
@Data
public class RabbitHelloMessageVo implements Serializable {
    private String code;
    private String msg;

}
