package top.codecrab.gulimall.coupon.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.response.R;
import top.codecrab.gulimall.coupon.entity.MemberPriceEntity;
import top.codecrab.gulimall.coupon.service.MemberPriceService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 商品会员价格
 *
 * @author codecrab
 * @date 2021-05-28 22:26:50
 */
@RestController
@RequestMapping("coupon/memberprice")
public class MemberPriceController {

    @Resource
    private MemberPriceService memberPriceService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberPriceService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        MemberPriceEntity memberPrice = memberPriceService.getById(id);

        return R.ok().put("memberPrice", memberPrice);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody MemberPriceEntity memberPrice) {
        memberPriceService.save(memberPrice);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody MemberPriceEntity memberPrice) {
        memberPriceService.updateById(memberPrice);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        memberPriceService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
