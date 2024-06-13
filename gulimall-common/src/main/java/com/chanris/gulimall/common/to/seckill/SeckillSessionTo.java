package com.chanris.gulimall.common.to.seckill;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/12
 * @description
 */
@Data
public class SeckillSessionTo {
    /**
     * id
     */
    private Long id;
    /**
     * 场次名称
     */
    private String name;
    /**
     * 每日开始时间
     */
    private Date startTime;
    /**
     * 每日结束时间
     */
    private Date endTime;
    /**
     * 启用状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private List<SeckillSkuRelationTo> relationSkus;
}
