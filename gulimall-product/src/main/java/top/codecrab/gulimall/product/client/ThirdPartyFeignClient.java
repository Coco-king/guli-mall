package top.codecrab.gulimall.product.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.codecrab.common.response.R;

import java.util.List;
import java.util.Map;

/**
 * @author codecrab
 * @since 2021年06月03日 17:23
 */
@FeignClient("gulimall-third-party")
public interface ThirdPartyFeignClient {
    @DeleteMapping("/oss/remove")
    R remove(@RequestBody Map<String, List<String>> params);
}
