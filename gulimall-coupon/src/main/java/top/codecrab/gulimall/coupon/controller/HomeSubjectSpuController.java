package top.codecrab.gulimall.coupon.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.R;
import top.codecrab.gulimall.coupon.entity.HomeSubjectSpuEntity;
import top.codecrab.gulimall.coupon.service.HomeSubjectSpuService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 专题商品
 *
 * @author codecrab
 * @date 2021-05-28 22:26:50
 */
@RestController
@RequestMapping("coupon/homesubjectspu")
public class HomeSubjectSpuController {

    @Resource
    private HomeSubjectSpuService homeSubjectSpuService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = homeSubjectSpuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        HomeSubjectSpuEntity homeSubjectSpu = homeSubjectSpuService.getById(id);

        return R.ok().put("homeSubjectSpu", homeSubjectSpu);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody HomeSubjectSpuEntity homeSubjectSpu) {
        homeSubjectSpuService.save(homeSubjectSpu);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody HomeSubjectSpuEntity homeSubjectSpu) {
        homeSubjectSpuService.updateById(homeSubjectSpu);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        homeSubjectSpuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
