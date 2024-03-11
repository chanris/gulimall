package com.chanris.gulimall.cart.service;

import com.chanris.gulimall.cart.vo.Cart;
import com.chanris.gulimall.cart.vo.CartItem;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author chenyue7@foxmail.com
 * @date 9/3/2024
 * @description
 */
public interface CartService {
    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    CartItem getCartItem(Long skuId);

    Cart getCart() throws ExecutionException, InterruptedException;

    void clearCartInfo(String cartKey);

    void checkItem(Long skuId, Integer checked);

    List<CartItem> getUserCartItems();

    void changeItemCount(Long skuId, Integer num);

    void deleteIdCartInfo(Integer skuId);
}
