package com.chanris.gulimall.common.to.mq;

import lombok.Data;

/**
 * @author chenyue7@foxmail.com
 * @date 23/2/2024
 * @description
 */

@Data
public class StockLockedTo {
    /** 库存工作单的id **/
    private Long id;
    /** 工作单详情的所有信息 **/
    private StockDetailTo detailTo;
}
