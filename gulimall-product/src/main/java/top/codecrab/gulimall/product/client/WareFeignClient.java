package top.codecrab.gulimall.product.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.codecrab.common.response.R;

import java.util.List;

/**
 * @author codecrab
 * @since 2021年05月29日 13:41
 */
@FeignClient("gulimall-ware")
public interface WareFeignClient {

    @PostMapping("/ware/waresku/has-stock")
    R hasStock(@RequestBody List<Long> skuIds);
}
