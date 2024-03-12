package com.chanris.gulimall.ware.service;

import com.chanris.gulimall.common.service.CrudService;
import com.chanris.gulimall.ware.dto.WareInfoDTO;
import com.chanris.gulimall.ware.entity.WareInfoEntity;
import com.chanris.gulimall.ware.feign.MemberFeignService;
import com.chanris.gulimall.ware.vo.FareVo;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 仓库信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 * 幂等性： 接口调用1次和调用100次数据库状态都是一样的。
 */
public interface WareInfoService extends CrudService<WareInfoEntity, WareInfoDTO> {

    FareVo getFare(Long addrId);
}