package com.chanris.gulimall.order.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.order.dao.OrderItemDao;
import com.chanris.gulimall.order.dto.OrderItemDTO;
import com.chanris.gulimall.order.entity.OrderItemEntity;
import com.chanris.gulimall.order.service.OrderItemService;
import com.chanris.gulimall.order.vo.RabbitHelloMessageVo;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * 订单项信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class OrderItemServiceImpl extends CrudServiceImpl<OrderItemDao, OrderItemEntity, OrderItemDTO> implements OrderItemService {

    @Override
    public QueryWrapper<OrderItemEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<OrderItemEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * queues: 声明需要监听的所有队列
     *
     * 参数列表：
     * 1.Message message： 原生消息
     * 2. T<发送的消息的类型>
     * 3. Channel channel: 当前传输的通道
     * Queue: 可以有很多人都来监听，只要收到消息，队列删除消息，只能有一个收到消息
     */
    @RabbitListener(queues = {"hello-java-queue"})
    public void receiveMessage(Message message, RabbitHelloMessageVo vo, Channel channel) throws IOException {
        System.out.println("接收到消息---内容：" + message + "===>类型：" + vo);

        // 签收消息， 非批量模式
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        // 拒收消息， requeue 重新入队 multiple 批量处理
        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
    }

}