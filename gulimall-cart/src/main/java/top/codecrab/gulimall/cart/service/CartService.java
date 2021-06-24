package top.codecrab.gulimall.cart.service;

import top.codecrab.gulimall.cart.vo.CartItemVo;

import java.util.concurrent.ExecutionException;

/**
 * @author codecrab
 * @since 2021年06月24日 16:55
 */
public interface CartService {
    CartItemVo getCartItemBySkuId(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    CartItemVo getCartItemByRedis(Long skuId);
}
