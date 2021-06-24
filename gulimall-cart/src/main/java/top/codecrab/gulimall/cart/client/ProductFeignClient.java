package top.codecrab.gulimall.cart.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.codecrab.common.response.R;

/**
 * @author codecrab
 * @since 2021年06月24日 18:29
 */
@FeignClient("gulimall-product")
public interface ProductFeignClient {

    @GetMapping("/product/skuinfo/info/{skuId}")
    R skuInfo(@PathVariable("skuId") Long skuId);

    @GetMapping("/product/skusaleattrvalue/list/{skuId}")
    R saleAttrListBySkuId(@PathVariable("skuId") Long skuId);

}
