package top.codecrab.gulimall.cart.service;

import top.codecrab.gulimall.cart.vo.CartItemVo;
import top.codecrab.gulimall.cart.vo.CartVo;

import java.util.concurrent.ExecutionException;

/**
 * @author codecrab
 * @since 2021年06月24日 16:55
 */
public interface CartService {
    CartItemVo addSkuToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    CartItemVo getCartItemByRedis(Long skuId);

    CartVo getCart() throws ExecutionException, InterruptedException;

    void checkItem(Long skuId, Boolean check);

    void changCount(Long skuId, Integer num);

    void removeItem(Long skuId);
}
