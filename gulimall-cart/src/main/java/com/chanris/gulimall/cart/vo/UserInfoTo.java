package com.chanris.gulimall.cart.vo;

import lombok.Data;

/**
 * @author chenyue7@foxmail.com
 * @date 9/3/2024
 * @description
 */
@Data
public class UserInfoTo {
    private Long userId;
    private String userKey;
    private boolean tempUser = false;
}
