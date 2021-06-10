package top.codecrab.gulimall.ware.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.codecrab.common.response.R;

/**
 * @author codecrab
 * @since 2021年06月08日 18:07
 */
@FeignClient("gulimall-product")
public interface ProductFeignClient {

    @GetMapping("/product/skuinfo/info/name/{skuId}")
    R skuInfoName(@PathVariable("skuId") Long skuId);

}
