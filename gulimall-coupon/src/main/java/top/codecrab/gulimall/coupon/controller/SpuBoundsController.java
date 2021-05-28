package top.codecrab.gulimall.coupon.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.R;
import top.codecrab.gulimall.coupon.entity.SpuBoundsEntity;
import top.codecrab.gulimall.coupon.service.SpuBoundsService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 商品spu积分设置
 *
 * @author codecrab
 * @date 2021-05-28 22:26:50
 */
@RestController
@RequestMapping("coupon/spubounds")
public class SpuBoundsController {

    @Resource
    private SpuBoundsService spuBoundsService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = spuBoundsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SpuBoundsEntity spuBounds = spuBoundsService.getById(id);

        return R.ok().put("spuBounds", spuBounds);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody SpuBoundsEntity spuBounds) {
        spuBoundsService.save(spuBounds);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody SpuBoundsEntity spuBounds) {
        spuBoundsService.updateById(spuBounds);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        spuBoundsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
