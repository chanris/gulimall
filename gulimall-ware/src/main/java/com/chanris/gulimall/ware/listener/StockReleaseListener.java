package com.chanris.gulimall.ware.listener;

import com.chanris.gulimall.common.to.OrderTo;
import com.chanris.gulimall.common.to.mq.StockDetailTo;
import com.chanris.gulimall.common.to.mq.StockLockedTo;
import com.chanris.gulimall.common.utils.Result;
import com.chanris.gulimall.ware.dto.WareOrderTaskDTO;
import com.chanris.gulimall.ware.dto.WareOrderTaskDetailDTO;
import com.chanris.gulimall.ware.service.WareSkuService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author chenyue7@foxmail.com
 * @date 17/3/2024
 * @description
 */
@Slf4j
@Service
@RabbitListener(queues = "stock.release.stock.queue")
public class StockReleaseListener {

    @Resource
    WareSkuService wareSkuService;

    /**
     * 库存自动解锁
     * 当用户没有在两分钟内消费 stock.delay.queue 内的消息， 就会发送给 stock.release.stock.queue 触发解锁库存操作
     * 1. 下单成功，库存锁定成功，接下来的业务调用失败，导致订单回滚。
     * 之前锁定的库存就要自动解锁
     * @param to
     * @param message
     */
    @RabbitHandler
    public void handleStockLockedRelease(StockLockedTo to, Message message, Channel channel) throws IOException {
        try {
            wareSkuService.unlockStock(to);
            log.info("发送自动释放库存ACK消息");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e) {
            log.info("发送自动释放库存REJECT消息");
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}
