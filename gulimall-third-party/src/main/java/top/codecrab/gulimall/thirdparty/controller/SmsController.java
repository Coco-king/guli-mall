package top.codecrab.gulimall.thirdparty.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.codecrab.common.response.R;
import top.codecrab.gulimall.thirdparty.utils.SmsUtils;

import java.util.Map;

/**
 * @author codecrab
 * @since 2021年06月21日 15:35
 */
@Slf4j
@RestController
@RequestMapping("/sms")
public class SmsController {

    /**
     * @param param {"code": "111111","phone": "phone","minute": 5,"templateCode": "xxx","useAccess": "true"}
     */
    @GetMapping("/send-code")
    public R sendCode(@RequestParam Map<String, Object> param) {
        try {
            SmsUtils.sendSms(param);
            return R.ok();
        } catch (Exception e) {
            log.error("验证码发送失败", e);
            return R.error();
        }
    }

}
