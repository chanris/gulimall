package com.chanris.gulimall.seckill.controller;

import com.chanris.gulimall.common.to.seckill.SeckillSkuRedisTo;
import com.chanris.gulimall.common.utils.Result;
import com.chanris.gulimall.seckill.service.SeckillService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/13
 * @description
 */
@RestController
public class SeckillController {

    @Resource
    private SeckillService seckillService;

    /**
     * 获取当前时间可以参与的秒杀获得商品信息
     * @return Result result
     */
    @GetMapping("/currentSeckillSkus")
    public Result<List<SeckillSkuRedisTo>> getCurrentSeckillSkus() {
        List<SeckillSkuRedisTo> data = seckillService.getCurrentSeckillSkus();
        return new Result<List<SeckillSkuRedisTo>>().ok(data);
    }

    /**
     * 根据商品id获得秒杀详细信息
     * @param skuId
     * @return
     */
    @GetMapping("/sku/seckill/{skuId}")
    public Result<SeckillSkuRedisTo> getSkuSeckillInfo(@PathVariable("skuId") Long skuId) {
        SeckillSkuRedisTo skuSeckillInfo = seckillService.getSkuSeckillInfo(skuId);
        return new Result<SeckillSkuRedisTo>().ok(skuSeckillInfo);
    }

    /**
     * 秒杀 -> 立即抢购 -> 登录判断 -> 合法性校验 ->  获得信号量 -> 成功（创建订单、确认支付） -> 发送mq
     * @param killId
     * @param key
     * @param num
     * @return
     */
    @GetMapping("/kill")
    public Result<String> seckill(@RequestParam("killId") String killId,
                          @RequestParam("key") String key,
                          @RequestParam("num") Integer num) {
        String res = seckillService.kill(killId, key, num);
        return new Result<String>().ok(res);
    }
}
