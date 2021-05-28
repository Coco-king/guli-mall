package top.codecrab.gulimall.coupon.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.R;
import top.codecrab.gulimall.coupon.entity.HomeAdvEntity;
import top.codecrab.gulimall.coupon.service.HomeAdvService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 首页轮播广告
 *
 * @author codecrab
 * @date 2021-05-28 22:26:50
 */
@RestController
@RequestMapping("coupon/homeadv")
public class HomeAdvController {

    @Resource
    private HomeAdvService homeAdvService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = homeAdvService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        HomeAdvEntity homeAdv = homeAdvService.getById(id);

        return R.ok().put("homeAdv", homeAdv);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody HomeAdvEntity homeAdv) {
        homeAdvService.save(homeAdv);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody HomeAdvEntity homeAdv) {
        homeAdvService.updateById(homeAdv);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        homeAdvService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
