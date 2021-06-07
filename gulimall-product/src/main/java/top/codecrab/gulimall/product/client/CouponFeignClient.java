package top.codecrab.gulimall.product.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.codecrab.common.response.R;
import top.codecrab.common.to.SkuReductionTo;
import top.codecrab.common.to.SpuBoundsTo;

/**
 * @author codecrab
 * @since 2021年05月29日 13:41
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignClient {

    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundsTo spuBoundsTo);

    @PostMapping("/coupon/skufullreduction/save/info")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
