package com.chanris.gulimall.ware.vo;

import com.chanris.gulimall.common.to.member.MemberReceiveAddressTo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenyue7@foxmail.com
 * @date 12/3/2024
 * @description
 */
@Data
public class FareVo {
    private MemberReceiveAddressTo address;
    private BigDecimal fare;
}
