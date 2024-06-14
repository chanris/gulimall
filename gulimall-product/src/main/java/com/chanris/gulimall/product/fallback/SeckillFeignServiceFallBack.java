package com.chanris.gulimall.product.fallback;

import com.chanris.gulimall.common.exception.CodeEnum;
import com.chanris.gulimall.common.to.seckill.SeckillSkuRedisTo;
import com.chanris.gulimall.common.utils.Result;
import com.chanris.gulimall.product.feign.SeckillFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/14
 * @description 服务熔断，接口的降级服务实现
 */
@Slf4j
@Component
public class SeckillFeignServiceFallBack implements SeckillFeignService {
    @Override
    public Result<SeckillSkuRedisTo> getSkuSeckillInfo(Long skuId) {
        log.info("【接口】gulimall-seckill:/sku/seckill/{skuId}：调用熔断保护方法，降级服务返回结果");
        return new Result<SeckillSkuRedisTo>().error(CodeEnum.TOO_MANY_REQUEST.code, CodeEnum.TOO_MANY_REQUEST.msg);
    }
}
