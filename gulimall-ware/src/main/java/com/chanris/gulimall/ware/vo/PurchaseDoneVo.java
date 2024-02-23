package com.chanris.gulimall.ware.vo;

import com.chanris.gulimall.common.validator.group.DefaultGroup;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 23/2/2024
 * @description
 */
@Data
public class PurchaseDoneVo {
    //采购单id
    @NotNull(groups = {DefaultGroup.class})
    private Long id;
    @NotEmpty(groups = {DefaultGroup.class})
    private List<PurchaseItemDoneVo> items;
}
