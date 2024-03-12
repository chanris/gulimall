package com.chanris.gulimall.order.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.common.to.SkuHasStockVo;
import com.chanris.gulimall.common.to.product.SpuInfoTo;
import com.chanris.gulimall.common.to.ware.FareTo;
import com.chanris.gulimall.common.to.ware.LockStockResultTo;
import com.chanris.gulimall.common.to.ware.OrderItemTo;
import com.chanris.gulimall.common.to.ware.WareSkuLockTo;
import com.chanris.gulimall.common.utils.Result;
import com.chanris.gulimall.common.vo.MemberResponseVo;
import com.chanris.gulimall.order.constant.OrderConstant;
import com.chanris.gulimall.order.dao.OrderDao;
import com.chanris.gulimall.order.dto.OrderDTO;
import com.chanris.gulimall.order.entity.OrderEntity;
import com.chanris.gulimall.order.entity.OrderItemEntity;
import com.chanris.gulimall.order.enums.OrderStatusEnum;
import com.chanris.gulimall.order.feign.CartFeignService;
import com.chanris.gulimall.order.feign.MemberFeignService;
import com.chanris.gulimall.order.feign.ProductFeignService;
import com.chanris.gulimall.order.feign.WmsFeignService;
import com.chanris.gulimall.order.interceptor.LoginUserInterceptor;
import com.chanris.gulimall.order.service.OrderItemService;
import com.chanris.gulimall.order.service.OrderService;
import com.chanris.gulimall.order.to.OrderCreateTo;
import com.chanris.gulimall.order.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.chanris.gulimall.order.constant.OrderConstant.USER_ORDER_TOKEN_PREFIX;

/**
 * 订单
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
@Slf4j
public class OrderServiceImpl extends CrudServiceImpl<OrderDao, OrderEntity, OrderDTO> implements OrderService {
    private final ThreadLocal<OrderSubmitVo> confirmVoThreadLocal = new ThreadLocal<>();
    @Resource
    private OrderDao orderDao;
    @Resource
    private MemberFeignService memberFeignService;
    @Resource
    private CartFeignService cartFeignService;
    @Resource
    private WmsFeignService wmsFeignService;
    @Resource
    private ThreadPoolExecutor executor;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private ProductFeignService productFeignService;
    @Resource
    private OrderItemService orderItemService;

    @Override
    public QueryWrapper<OrderEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<OrderEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * feign 远程调用时，丢失请求头信息 解决： 加 feign的 请求拦截器
     * feign 使用ThreadLocal 共享 请求
     *
     * @return
     */
    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        OrderConfirmVo confirmVo = new OrderConfirmVo();
        MemberResponseVo memberResponseVo = LoginUserInterceptor.loginUser.get();
        // 解决feign 远程调用 在异步环境下，请求头信息丢失问题
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        CompletableFuture<Void> getAddress = CompletableFuture.runAsync(() -> {
            // 1.远程查询所有的收获地址列表
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<MemberAddressVo> address = memberFeignService.getAddress(memberResponseVo.getId());
            confirmVo.setAddress(address);
        }, executor);

        CompletableFuture<Void> getCartItem = CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            // 2. 远程查询购物车所有选中的购物项
            List<OrderItemVo> cartItems = cartFeignService.getCurrentCartItems();
            if (cartItems == null || cartItems.size() == 0) {
                log.warn("远程查询购物车为空");
            }
            confirmVo.setItems(cartItems);
        }, executor).thenRunAsync(() -> {
            List<OrderItemVo> items = confirmVo.getItems();
            List<Long> skuIds = items.stream().map(OrderItemVo::getSkuId).collect(Collectors.toList());
            // 批量远程查询 商品列表是否有库存
            Result<List<SkuHasStockVo>> r = wmsFeignService.getSkusHasStock(skuIds);
            if (r.success()) {
                List<SkuHasStockVo> hasStockVos = r.getData();
                if (confirmVo.getStocks() == null) {
                    confirmVo.setStocks(new HashMap<>());
                }
                Map<Long, Boolean> stocks = confirmVo.getStocks();
                hasStockVos.forEach(item -> {
                    stocks.put(item.getSkuId(), item.getHasStock());
                });
            }
        });

        // 3.查询用户积分
        Integer integration = memberResponseVo.getIntegration();
        confirmVo.setIntegration(integration);

        //4. 其他属性自动计算
        CompletableFuture.allOf(getAddress, getCartItem).get();

        // todo 5. 防重令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        stringRedisTemplate.opsForValue().set(USER_ORDER_TOKEN_PREFIX + memberResponseVo.getId(), token, 30, TimeUnit.MINUTES);
        confirmVo.setOrderToken(token);
        return confirmVo;
    }

    /**
     * 下单操作
     *
     * @param vo
     * @return
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public SubmitOrderRespVo submitOrder(OrderSubmitVo vo) {
        confirmVoThreadLocal.set(vo);
        SubmitOrderRespVo response = new SubmitOrderRespVo();
        response.setCode(0);
        // 下单；验证令牌、创建订单、验证价格、锁库存
        MemberResponseVo memberResponseVo = LoginUserInterceptor.loginUser.get();
        String orderToken = vo.getOrderToken();
        // 使用lua脚本 保证 对比和删除令牌操作的原子性
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Long result = stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), List.of(USER_ORDER_TOKEN_PREFIX + memberResponseVo.getId()), orderToken);
        if (result == null || !result.equals(1L)) {
            // 令牌验证失败
            response.setCode(1);
            return response;
        }
        // 1. 创建订单
        OrderCreateTo order = createOrder();
        BigDecimal payAmount = order.getOrder().getPayAmount();
        BigDecimal payPrice = vo.getPayPrice();
        // 2. 验价
        if (Math.abs(payAmount.subtract(payPrice).doubleValue()) > 0.01) {
            response.setCode(2);
            return response;
        }

        //3. 保存订单
        saveOrder(order);

        //4. 库存锁定
        WareSkuLockTo lockTo = new WareSkuLockTo();
        lockTo.setOrderSn(order.getOrder().getOrderSn());
        List<OrderItemTo> itemTos = order.getOrderItems().stream().map(item -> {
            OrderItemTo to = new OrderItemTo();
            to.setSkuId(item.getSkuId());
            to.setCount(item.getSkuQuantity());
            item.setSkuName(item.getSkuName());
            return to;
        }).toList();
        lockTo.setLocks(itemTos);
        Result<?> r = wmsFeignService.orderLockStock(lockTo);
        if (!r.success()) {
            response.setCode(3);
            return response;
        }

        response.setOrder(order.getOrder());
        return response;
    }

    /**
     * 保存订单所有数据
     *
     * @param orderCreateTo
     */
    private void saveOrder(OrderCreateTo orderCreateTo) {

        //获取订单信息
        OrderEntity order = orderCreateTo.getOrder();
        //保存订单
        orderDao.insert(order);

        //获取订单项信息
        List<OrderItemEntity> orderItems = orderCreateTo.getOrderItems();
        //批量保存订单项数据
        orderItemService.insertBatch(orderItems);
    }

    private OrderCreateTo createOrder() {
        OrderCreateTo createTo = new OrderCreateTo();
        //1、生成订单号
        String orderSn = IdWorker.getTimeId(); // mybatis 生成时间ID
        OrderEntity entity = new OrderEntity();
        entity.setOrderSn(orderSn);
        // 获得下单信息
        OrderSubmitVo submitVo = confirmVoThreadLocal.get();
        // 远程获得运费&收货人信息
        Result<FareTo> r = wmsFeignService.getFare(submitVo.getAddrId());
        if (r.success()) {
            FareTo fareTo = r.getData();
            // 运费信息
            entity.setFreightAmount(fareTo.getFare());
            // 收货人信息
            entity.setReceiverCity(fareTo.getAddress().getCity());
            entity.setReceiverDetailAddress(fareTo.getAddress().getDetailAddress());
            entity.setReceiverName(fareTo.getAddress().getName());
            entity.setReceiverPhone(fareTo.getAddress().getPhone());
            entity.setReceiverPostCode(fareTo.getAddress().getPostCode());
            entity.setReceiverRegion(fareTo.getAddress().getRegion());
        }
        // 获得所有订单项信息
        List<OrderItemEntity> orderItemEntities = buildOderItems(orderSn);
        createTo.setOrderItems(orderItemEntities);
        createTo.setOrder(entity);

        // 3. 验价
        computePrice(entity, orderItemEntities);
        return createTo;
    }

    //计算价格
    private void computePrice(OrderEntity entity, List<OrderItemEntity> itemEntities) {
        // 1. 订单价格数据相关
        BigDecimal total = new BigDecimal("0.0");
        BigDecimal coupon = new BigDecimal("0.0");
        BigDecimal promotion = new BigDecimal("0.0");
        BigDecimal intergration = new BigDecimal("0.0");
        int gift = 0;
        int growth = 0;
        for (OrderItemEntity item : itemEntities) {
            promotion = promotion.add(item.getPromotionAmount());
            coupon = coupon.add(item.getCouponAmount());
            intergration = intergration.add(item.getIntegrationAmount());
            total = total.add(item.getRealAmount());
            gift += item.getGiftIntegration();
            growth += item.getGiftGrowth();
        }
        // 1. 订单价格相关
        entity.setTotalAmount(total);
        // 应付总额（加上 运费）
        entity.setPayAmount(total.add(entity.getFreightAmount()));
        // 打折优惠金额
        entity.setIntegrationAmount(intergration);
        entity.setPromotionAmount(promotion);
        entity.setCouponAmount(coupon);

        // 设置订单状态
        entity.setStatus(OrderStatusEnum.CREATE_NEW.code);
        entity.setAutoConfirmDay(7);
        entity.setGrowth(growth);
        entity.setIntegration(gift);
    }

    // 构建所有订单项数据
    private List<OrderItemEntity> buildOderItems(String orderSn) {
        List<OrderItemVo> currentCartItems = cartFeignService.getCurrentCartItems();
        if (currentCartItems != null &&currentCartItems.size() > 0) {
            List<OrderItemEntity> orderItemEntities = currentCartItems.stream().map(cartItem -> {
                OrderItemEntity itemEntity = buildOderItem(cartItem);
                itemEntity.setOrderSn(orderSn);

                return itemEntity;
            }).toList();
            return orderItemEntities;
        }
        return null;
    }

    // 构建一个订单项数据
    private OrderItemEntity buildOderItem(OrderItemVo cartItem) {
        OrderItemEntity itemEntity = new OrderItemEntity();
        // 1. 订单信息：订单号
        // 2. 商品的sku信息
        itemEntity.setSkuId(cartItem.getSkuId());
        itemEntity.setSkuName(cartItem.getTitle());
        itemEntity.setSkuPic(cartItem.getImage());
        itemEntity.setSkuAttrsVals(StringUtils.collectionToDelimitedString(cartItem.getSkuAttrValues(), ";"));
        itemEntity.setSkuQuantity(cartItem.getCount());
        itemEntity.setSkuPrice(cartItem.getPrice());
        // 3.商品的spu信息
        Result<SpuInfoTo> result = productFeignService.getSpuInfoBySkuId(cartItem.getSkuId());
        if (result.success()) {
            SpuInfoTo spuInfoTo = result.getData();
            itemEntity.setSpuId(spuInfoTo.getId());
            itemEntity.setSpuBrand(spuInfoTo.getBrandId().toString());
            itemEntity.setSpuName(spuInfoTo.getSpuName());
            itemEntity.setCategoryId(spuInfoTo.getCatalogId());
        }
        // 4. 优惠信息【不做】
        // 5. 积分信息
        itemEntity.setGiftGrowth(cartItem.getPrice().multiply(new BigDecimal(cartItem.getCount().toString())).intValue());
        itemEntity.setGiftIntegration(cartItem.getPrice().multiply(new BigDecimal(cartItem.getCount().toString())).intValue());
        // 6.订单项价格信息
        itemEntity.setPromotionAmount(new BigDecimal("0"));
        itemEntity.setCouponAmount(new BigDecimal("0"));
        itemEntity.setIntegrationAmount(new BigDecimal("0"));
        // 当前订单项的实际金额
        BigDecimal origin = itemEntity.getSkuPrice().multiply(new BigDecimal(itemEntity.getSkuQuantity()));
        BigDecimal realAmount = origin
                .subtract(itemEntity.getPromotionAmount())
                .subtract(itemEntity.getCouponAmount())
                .subtract(itemEntity.getIntegrationAmount());
        itemEntity.setRealAmount(realAmount);
        return itemEntity;
    }
}