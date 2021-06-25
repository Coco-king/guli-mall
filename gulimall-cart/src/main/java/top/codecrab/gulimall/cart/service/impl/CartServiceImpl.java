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
import top.codecrab.gulimall.cart.vo.CartVo;
import top.codecrab.gulimall.cart.vo.SkuInfoVo;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

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
    public CartItemVo addSkuToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {

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
        return (CartItemVo) getCartRedisOps().get(skuId.toString());
    }

    @Override
    public CartVo getCart() throws ExecutionException, InterruptedException {
        String key = "";
        CartVo cartVo = new CartVo();

        UserInfoTo info = CartInterceptor.THREAD_LOCAL.get();
        key = CartConstant.CART_ITEM_PREFIX + info.getUserKey();
        List<CartItemVo> cartItems = getCartItems(key);
        if (info.getUserId() != null) {
            if (cartItems != null) {
                //如果用户登录就把临时购物车添加到用户购物车
                for (CartItemVo cartItemVo : cartItems) {
                    addSkuToCart(cartItemVo.getSkuId(), cartItemVo.getCount());
                }

                //清除临时购物车
                List<String> skuIds = cartItems.stream()
                        .map(cartItemVo -> cartItemVo.getSkuId().toString())
                        .collect(Collectors.toList());
                clearCart(skuIds, CartConstant.CART_ITEM_PREFIX + info.getUserKey());
            }

            //获取已登录用户的购物车
            key = CartConstant.CART_ITEM_PREFIX + info.getUserId();
            List<CartItemVo> list = getCartItems(key);
            cartVo.setItems(list);
        } else {
            //未登录
            cartVo.setItems(cartItems);
        }

        BigDecimal total = BigDecimal.ZERO;
        for (CartItemVo item : cartVo.getItems()) {
            total = total.add(item.getTotalPrice());
        }

        cartVo.setTotalAmount(total);
        return cartVo;
    }

    @Override
    public void checkItem(Long skuId, Boolean check) {
        CartItemVo itemVo = getCartItemByRedis(skuId);
        if (itemVo != null) {
            itemVo.setCheck(check);
            getCartRedisOps().put(skuId.toString(), itemVo);
        }
    }

    @Override
    public void changCount(Long skuId, Integer num) {
        CartItemVo vo = getCartItemByRedis(skuId);
        if (vo != null) {
            vo.setCount(num);
            getCartRedisOps().put(skuId.toString(), vo);
        }
    }

    @Override
    public void removeItem(Long skuId) {
        getCartRedisOps().delete(skuId.toString());
    }

    private void clearCart(List<String> skuIds, String key) {
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(key);
        for (String skuId : skuIds) {
            ops.delete(skuId);
        }
    }

    private List<CartItemVo> getCartItems(String key) {
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(key);
        List<Object> values = ops.values();
        if (values != null && !values.isEmpty()) {
            return values.stream()
                    .map(o -> (CartItemVo) o)
                    .collect(Collectors.toList());
        }
        return null;
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
