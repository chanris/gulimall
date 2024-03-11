package com.chanris.gulimall.order.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.common.vo.MemberResponseVo;
import com.chanris.gulimall.order.dao.OrderDao;
import com.chanris.gulimall.order.dto.OrderDTO;
import com.chanris.gulimall.order.entity.OrderEntity;
import com.chanris.gulimall.order.feign.CartFeignService;
import com.chanris.gulimall.order.feign.MemberFeignService;
import com.chanris.gulimall.order.interceptor.LoginUserInterceptor;
import com.chanris.gulimall.order.service.OrderService;
import com.chanris.gulimall.order.vo.MemberAddressVo;
import com.chanris.gulimall.order.vo.OrderConfirmVo;
import com.chanris.gulimall.order.vo.OrderItemVo;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 订单
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class OrderServiceImpl extends CrudServiceImpl<OrderDao, OrderEntity, OrderDTO> implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private MemberFeignService memberFeignService;

    @Resource
    private CartFeignService cartFeignService;

    @Resource
    private ThreadPoolExecutor executor;

    @Override
    public QueryWrapper<OrderEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<OrderEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * feign 远程调用时，丢失请求头信息 解决： 加 feign的 请求拦截器
     * feign 使用ThreadLocal 共享 请求
     * @return
     */
    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        OrderConfirmVo confirmVo = new OrderConfirmVo();
        MemberResponseVo  memberResponseVo = LoginUserInterceptor.loginUser.get();
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
            List<OrderItemVo> items = cartFeignService.getCurrentCartItems();
            confirmVo.setItems(items);
        }, executor);


        // 3.查询用户积分
        Integer integration = memberResponseVo.getIntegration();
        confirmVo.setIntegration(integration);

        //4. 其他属性自动计算

        // todo 5. 防重令牌
        CompletableFuture.allOf(getAddress, getCartItem).get();
        return confirmVo;
    }
}