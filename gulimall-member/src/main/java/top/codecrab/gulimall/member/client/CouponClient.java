package top.codecrab.gulimall.member.client;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author codecrab
 * @since 2021年05月29日 13:41
 */
@FeignClient("gulimall-coupon")
public interface CouponClient {

}
