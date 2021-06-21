package top.codecrab.gulimall.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.codecrab.common.response.R;

import java.util.Map;

/**
 * @author codecrab
 * @since 2021年06月21日 16:00
 */
@FeignClient("gulimall-third-party")
public interface ThirdPartyFeignClient {

    /**
     * @param param {"code": "111111","phone": "phone","minute": 5,"templateCode": "xxx","useAccess": "true"}
     */
    @GetMapping("/sms/send-code")
    R sendCode(@RequestParam Map<String, Object> param);

}
