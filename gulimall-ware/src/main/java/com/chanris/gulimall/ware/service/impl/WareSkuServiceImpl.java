package com.chanris.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.common.to.OrderTo;
import com.chanris.gulimall.common.to.mq.StockDetailTo;
import com.chanris.gulimall.common.to.mq.StockLockedTo;
import com.chanris.gulimall.common.utils.Result;
import com.chanris.gulimall.ware.dao.WareSkuDao;
import com.chanris.gulimall.ware.dto.WareOrderTaskDTO;
import com.chanris.gulimall.ware.dto.WareOrderTaskDetailDTO;
import com.chanris.gulimall.ware.dto.WareSkuDTO;
import com.chanris.gulimall.ware.entity.WareOrderTaskDetailEntity;
import com.chanris.gulimall.ware.entity.WareOrderTaskEntity;
import com.chanris.gulimall.ware.entity.WareSkuEntity;
import com.chanris.gulimall.ware.exception.NoStockException;
import com.chanris.gulimall.ware.feign.OrderFeignService;
import com.chanris.gulimall.ware.service.WareOrderTaskDetailService;
import com.chanris.gulimall.ware.service.WareOrderTaskService;
import com.chanris.gulimall.ware.service.WareSkuService;
import cn.hutool.core.util.StrUtil;
import com.chanris.gulimall.ware.vo.OrderItemVo;
import com.chanris.gulimall.ware.vo.SkuHasStockVo;
import com.chanris.gulimall.ware.vo.WareSkuLockVo;
import com.rabbitmq.client.Channel;
import lombok.Data;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class WareSkuServiceImpl extends CrudServiceImpl<WareSkuDao, WareSkuEntity, WareSkuDTO> implements WareSkuService {

    @Resource
    private WareSkuDao wareSkuDao;
    @Resource
    private WareOrderTaskService orderTaskService;
    @Resource
    private WareOrderTaskDetailService wareOrderTaskDetailService;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private OrderFeignService orderFeignService;


    private void unlockStock(Long skuId, Long wareId, Integer num, Long taskDetailId) {
        //库存解锁
        wareSkuDao.unLockStock(skuId,wareId,num);

        //更新工作单的状态
        WareOrderTaskDetailEntity taskDetailEntity = new WareOrderTaskDetailEntity();
        taskDetailEntity.setId(taskDetailId);
        //变为已解锁
        taskDetailEntity.setLockStatus(2);
        wareOrderTaskDetailService.updateById(taskDetailEntity);
    }

    @Override
    public QueryWrapper<WareSkuEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public WareSkuEntity getByWrapper(QueryWrapper<WareSkuEntity> wrapper) {
        return wareSkuDao.selectOne(wrapper);
    }

    @Override
    public List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds) {
//        skuIds.stream().map(sku -> {
//            SkuHasStockVo vo = new SkuHasStockVo();
//           //select sum(stock - stoc k_locked) from wms_ware_sku where sku_id = 1
//            //查询当前sku的总库存量
//            return vo;
//        })

        return wareSkuDao.getSkuHasStock(skuIds);
    }

    /**
     * 锁定库存
     *
     *  库存解锁的场景
     *  1. 下订单成功，订单过期没有支付被系统自动取消
     *  2. 下单成功，库存锁定，接下来的业务调用失败，导致订单回滚
     * @param vo
     * @return
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public boolean orderLockStock(WareSkuLockVo vo) {
        // 保存库存工作单详情
        WareOrderTaskEntity taskEntity = new WareOrderTaskEntity();
        taskEntity.setOrderSn(vo.getOrderSn());
        orderTaskService.insert(taskEntity);

        // 1.找到商品在哪个仓库有库存
        List<OrderItemVo> locks = vo.getLocks();
        List<SkuWareHasStock> hasStocks = locks.stream().map(item -> {
            SkuWareHasStock stock = new SkuWareHasStock();
            Long skuId = item.getSkuId();
            List<Long> ids = wareSkuDao.listWareIdHasSkuStock(skuId);
            stock.setWareId(ids);
            stock.setNum(item.getCount());
            stock.setSkuId(skuId);
            return stock;
        }).toList();

        boolean allLock = true;
        // 锁定库存
        for (SkuWareHasStock hasStock : hasStocks) {
            boolean skuStocked = false;
            Long skuId = hasStock.getSkuId();
            List<Long> wareIds = hasStock.getWareId();
            if(wareIds == null || wareIds.size() == 0) {
                throw new NoStockException("库存不足 skuId:" + skuId);
            }
            for(Long wareId: wareIds) {
                long count = wareSkuDao.lockSkuStock(skuId, wareId, hasStock.getNum());
                if(count == 1L) {
                    skuStocked = true; // 库存锁定成功
                    // 告诉MQ 库存锁定成功
                    WareOrderTaskDetailEntity taskDetailEntity = new WareOrderTaskDetailEntity(null, skuId, "", hasStock.getNum(), taskEntity.getId(), wareId, 1);
                    wareOrderTaskDetailService.insert(taskDetailEntity);
                    StockLockedTo lockedTo = new StockLockedTo();
                    lockedTo.setId(taskEntity.getId());
                    StockDetailTo detailTo = new StockDetailTo();
                    BeanUtils.copyProperties(taskDetailEntity, detailTo);
                    lockedTo.setDetailTo(detailTo);
                    rabbitTemplate.convertAndSend("stock-event-exchange","stock.locked", lockedTo);
                    break;
                }
            }
            if(!skuStocked) {
                throw new NoStockException("商品没有库存 skuId:" + skuId);
            }
        }
        return true;
    }

    @Override
    public void unlockStock(StockLockedTo to) throws RuntimeException {
        System.out.println("收到解锁库存的消息");
        Long id = to.getId();
        StockDetailTo detailTo = to.getDetailTo();
        Long skuId = detailTo.getSkuId();
        Long detailId = detailTo.getId();
        // 解锁
        // 1.查询数据库关于这个订单的锁定库存信息
        // 有，证明库存锁定成功
        // 解锁： 订单详情
        // 1. 没有订单。必须解锁
        // 2. 有这个订单。不是解锁库存
        //       订单状态；已取消，解锁库存
        //               没取消，不能解锁
        //  没有，库存锁定失败，库存回滚了，无需解锁
        WareOrderTaskDetailDTO detailDTO = wareOrderTaskDetailService.get(detailId);
        if (detailDTO != null) {
            //解锁
            WareOrderTaskDTO orderTaskDTO = orderTaskService.get(id);
            Result<OrderTo> r = orderFeignService.getOrderStatus(orderTaskDTO.getOrderSn());
            if (r.success()) {
                // 订单数据返回成功
                OrderTo data = r.getData();
                if (data == null || data.getStatus() == 4) { //  订单 不存在 或 已关闭 解锁库存
                    //订单取消,才能解锁库存
                    if (detailDTO.getLockStatus() == 1) {
                        unlockStock(skuId, detailTo.getWareId(), detailDTO.getSkuNum(), detailId);
                    }
                }
            }else {
                throw new RuntimeException("远程获得订单信息失败");
            }
        }
    }

    @Data
    class SkuWareHasStock {
        private Long skuId;
        private List<Long> wareId;
        private Integer num;
    }
}