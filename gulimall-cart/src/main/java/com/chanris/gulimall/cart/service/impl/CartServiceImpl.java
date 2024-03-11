package com.chanris.gulimall.cart.service.impl;

import com.alibaba.fastjson2.JSON;
import com.chanris.gulimall.cart.exception.CartException;
import com.chanris.gulimall.cart.feign.ProductFeignService;
import com.chanris.gulimall.cart.interceptor.CartInterceptor;
import com.chanris.gulimall.cart.service.CartService;
import com.chanris.gulimall.cart.vo.Cart;
import com.chanris.gulimall.cart.vo.CartItem;
import com.chanris.gulimall.cart.vo.UserInfoTo;
import com.chanris.gulimall.common.to.product.SkuInfoTo;
import com.chanris.gulimall.common.utils.Result;

import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author chenyue7@foxmail.com
 * @date 9/3/2024
 * @description
 */
@Service
public class CartServiceImpl implements CartService {

    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    ProductFeignService productFeignService;
    @Resource
    ThreadPoolExecutor executor;

    private final String CART_PREFIX = "gulimall:cart:";

    /**
     * 向购物车添加商品
     *
     * @param skuId
     * @param num
     * @return
     */
    @Override
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        // 有商品直接加数量
        String ct = (String) cartOps.get(skuId.toString());
        if (StringUtils.hasLength(ct)) {
            CartItem item = JSON.parseObject(ct, CartItem.class);
            item.setCount(item.getCount() + 1);
            cartOps.put(skuId.toString(), JSON.toJSONString(item));
            return item;
        }

        CartItem cartItem = new CartItem();
        CompletableFuture<Void> getSkuInfo = CompletableFuture.runAsync(() -> {
            Result<SkuInfoTo> result = productFeignService.get(skuId);
            if (result.success()) {
                SkuInfoTo skuInfoTo = result.getData();
                cartItem.setSkuId(skuInfoTo.getSkuId());
                cartItem.setImage(skuInfoTo.getSkuDefaultImg());
                cartItem.setCount(num);
                cartItem.setCheck(true);
                cartItem.setPrice(skuInfoTo.getPrice());
                cartItem.setTitle(skuInfoTo.getSkuTitle());
            }
        }, executor);

        // 远程查询 商品的属性信息
        CompletableFuture<Void> getSkuAttrValues = CompletableFuture.runAsync(() -> {
            List<String> attrValues = productFeignService.getSkuSaleAttrValues(skuId);
            cartItem.setSkuAttrValues(attrValues);
        });

        CompletableFuture.allOf(getSkuInfo, getSkuAttrValues).get();//阻塞
        String s = JSON.toJSONString(cartItem);
        cartOps.put(skuId.toString(), s);
        return cartItem;
    }

    @Override
    public CartItem getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String str = (String) cartOps.get(skuId.toString());
        CartItem cartItem = JSON.parseObject(str, CartItem.class);
        return cartItem;
    }

    @Override
    public Cart getCart() throws ExecutionException, InterruptedException {
        Cart cart = new Cart();
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        if (userInfoTo.isTempUser()) {
            String cartKey = CART_PREFIX + userInfoTo.getUserKey();
            List<CartItem> cartItems = getCartItems(cartKey);
            cart.setItems(cartItems);
        } else {
            //1、登录
            String cartKey = CART_PREFIX + userInfoTo.getUserId();
            //临时购物车的键
            String temptCartKey = CART_PREFIX + userInfoTo.getUserKey();

            //2、如果临时购物车的数据还未进行合并
            List<CartItem> tempCartItems = getCartItems(temptCartKey);
            if (tempCartItems != null) {
                //临时购物车有数据需要进行合并操作
                for (CartItem item : tempCartItems) {
                    addToCart(item.getSkuId(), item.getCount());
                }
                //清除临时购物车的数据
                clearCartInfo(temptCartKey);
            }

            //3、获取登录后的购物车数据【包含合并过来的临时购物车的数据和登录后购物车的数据】
            List<CartItem> cartItems = getCartItems(cartKey);
            cart.setItems(cartItems);
        }
        return cart;
    }

    @Override
    public void clearCartInfo(String cartKey) {
        stringRedisTemplate.delete(cartKey);
    }

    @Override
    public void checkItem(Long skuId, Integer check) {
        //查询购物车里面的商品
        CartItem cartItem = getCartItem(skuId);
        //修改商品状态
        cartItem.setCheck(check == 1);

        //序列化存入redis中
        String redisValue = JSON.toJSONString(cartItem);

        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.put(skuId.toString(), redisValue);
    }

    @Override
    public List<CartItem> getUserCartItems() {
        List<CartItem> cartItemVoList;
        //获取当前用户登录的信息
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        //如果用户未登录直接返回null
        if (userInfoTo.getUserId() == null) {
            return null;
        } else {
            //获取购物车项
            String cartKey = CART_PREFIX + userInfoTo.getUserId();
            //获取所有的
            List<CartItem> cartItems = getCartItems(cartKey);
            if (cartItems == null) {
                throw new CartException();
            }
            //筛选出选中的
            cartItemVoList = cartItems.stream()
                    .filter(CartItem::getCheck)
                    .peek(item -> {
                        //更新为最新的价格（查询数据库）
                        // TODO 11/3/24 查询每件商品的实际价格 （循环调用 浪费资源，需要优化）
                        BigDecimal price = productFeignService.getPrice(item.getSkuId());
                        item.setPrice(price);
                    }).collect(Collectors.toList());
        }
        return cartItemVoList;
    }

    /**
     * 获取购物车里面的数据
     *
     * @param cartKey
     * @return
     */
    private List<CartItem> getCartItems(String cartKey) {
        //获取购物车里面的所有商品
        BoundHashOperations<String, Object, Object> operations = stringRedisTemplate.boundHashOps(cartKey);
        List<Object> values = operations.values();
        if (values != null && values.size() > 0) {
            return values.stream().map((obj) -> {
                String str = (String) obj;
                return JSON.parseObject(str, CartItem.class);
            }).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 获取到我们要操作的购物车
     *
     * @return
     */
    private BoundHashOperations<String, Object, Object> getCartOps() {
        //先得到当前用户信息
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();

        String cartKey = "";
        if (userInfoTo.getUserId() != null) {
            //gulimall:cart:1
            cartKey = CART_PREFIX + userInfoTo.getUserId();
        } else {
            cartKey = CART_PREFIX + userInfoTo.getUserKey();
        }

        //绑定指定的key操作Redis
        BoundHashOperations<String, Object, Object> operations = stringRedisTemplate.boundHashOps(cartKey);

        return operations;
    }

    @Override
    public void changeItemCount(Long skuId, Integer num) {
        //查询购物车里面的商品
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCount(num);

        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        //序列化存入redis中
        String redisValue = JSON.toJSONString(cartItem);
        cartOps.put(skuId.toString(), redisValue);
    }

    @Override
    public void deleteIdCartInfo(Integer skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.delete(skuId.toString());
    }
}
