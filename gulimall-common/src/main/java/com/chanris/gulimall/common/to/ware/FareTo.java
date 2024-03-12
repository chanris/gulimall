package com.chanris.gulimall.common.to.ware;

import com.chanris.gulimall.common.to.member.MemberReceiveAddressTo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenyue7@foxmail.com
 * @date 12/3/2024
 * @description
 */
@Data
public class FareTo {
    private MemberReceiveAddressTo address;
    private BigDecimal fare;
}
