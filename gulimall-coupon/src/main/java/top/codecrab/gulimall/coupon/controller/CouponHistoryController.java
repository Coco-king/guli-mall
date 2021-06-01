package top.codecrab.gulimall.coupon.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.response.R;
import top.codecrab.gulimall.coupon.entity.CouponHistoryEntity;
import top.codecrab.gulimall.coupon.service.CouponHistoryService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 优惠券领取历史记录
 *
 * @author codecrab
 * @date 2021-05-28 22:26:50
 */
@RestController
@RequestMapping("coupon/couponhistory")
public class CouponHistoryController {

    @Resource
    private CouponHistoryService couponHistoryService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = couponHistoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        CouponHistoryEntity couponHistory = couponHistoryService.getById(id);

        return R.ok().put("couponHistory", couponHistory);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody CouponHistoryEntity couponHistory) {
        couponHistoryService.save(couponHistory);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody CouponHistoryEntity couponHistory) {
        couponHistoryService.updateById(couponHistory);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        couponHistoryService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
