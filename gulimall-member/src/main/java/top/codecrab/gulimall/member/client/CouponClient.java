package top.codecrab.gulimall.member.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import top.codecrab.common.utils.R;

/**
 * @author codecrab
 * @since 2021年05月29日 13:41
 */
@FeignClient("gulimall-coupon")
public interface CouponClient {

    /**
     * 查询优惠列表
     */
    @GetMapping("coupon/coupon/member/list")
    R memberCoupons();
}
