/** Copyright 2020 bejson.com */
package com.chanris.gulimall.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenyue7@foxmail.com
 * @date 23/2/2024
 * @description
 */

@Data
public class MemberPrice {

  private Long id;
  private String name;
  private BigDecimal price;

}
