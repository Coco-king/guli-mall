package top.codecrab.gulimall.cart.service.impl;

import cn.hutool.core.lang.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.codecrab.common.constant.CartConstant;
import top.codecrab.common.response.R;
import top.codecrab.gulimall.cart.client.ProductFeignClient;
import top.codecrab.gulimall.cart.interceptor.CartInterceptor;
import top.codecrab.gulimall.cart.service.CartService;
import top.codecrab.gulimall.cart.to.UserInfoTo;
import top.codecrab.gulimall.cart.vo.CartItemVo;
import top.codecrab.gulimall.cart.vo.SkuInfoVo;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author codecrab
 * @since 2021年06月24日 16:55
 */
@Slf4j
@Service("cartService")
public class CartServiceImpl implements CartService {

    @Resource
    private ProductFeignClient productFeignClient;

    @Resource
    private ThreadPoolExecutor executor;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public CartItemVo getCartItemBySkuId(Long skuId, Integer num) throws ExecutionException, InterruptedException {

        BoundHashOperations<String, Object, Object> redisOps = getCartRedisOps();

        CartItemVo item = (CartItemVo) redisOps.get(skuId.toString());
        if (item != null) {
            //更新购物车商品数量
            item.setCount(item.getCount() + num);
            //更新redis
            redisOps.put(skuId.toString(), item);

            return item;
        }

        CartItemVo cartItem = new CartItemVo();

        CompletableFuture<Void> skuInfo = CompletableFuture.runAsync(() -> {
            R r = productFeignClient.skuInfo(skuId);
            SkuInfoVo sku = r.getData("skuInfo", new TypeReference<SkuInfoVo>() {
            });
            cartItem.setSkuId(skuId);
            cartItem.setTitle(sku.getSkuTitle());
            cartItem.setDefaultImg(sku.getSkuDefaultImg());
            cartItem.setPrice(sku.getPrice());
            cartItem.setCount(num);
            cartItem.setTotalPrice(cartItem.getTotalPrice());
        }, executor);

        CompletableFuture<Void> saleAttrListBySkuId = CompletableFuture.runAsync(() -> {
            R r = productFeignClient.saleAttrListBySkuId(skuId);
            List<String> list = r.getFeignData(new TypeReference<List<String>>() {
            });
            cartItem.setSkuAttrs(list);
        }, executor);

        CompletableFuture.allOf(skuInfo, saleAttrListBySkuId).get();

        //存入redis
        redisOps.put(skuId.toString(), cartItem);
        return cartItem;
    }

    @Override
    public CartItemVo getCartItemByRedis(Long skuId) {
        Object o = getCartRedisOps().get(skuId.toString());
        if (o == null) {
            return new CartItemVo();
        }
        return (CartItemVo) o;
    }

    private BoundHashOperations<String, Object, Object> getCartRedisOps() {
        String key = "";

        UserInfoTo info = CartInterceptor.THREAD_LOCAL.get();
        if (info.getUserId() == null) {
            //使用临时用户的key
            key = CartConstant.CART_ITEM_PREFIX + info.getUserKey();
        } else {
            //使用用户ID
            key = CartConstant.CART_ITEM_PREFIX + info.getUserId();
        }

        return redisTemplate.boundHashOps(key);
    }

}
