package com.chanris.gulimall.product.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttrRespVo extends AttrVo {


    private String catelogName;

    private String groupName;

    private Long[] catelogPath;

}
