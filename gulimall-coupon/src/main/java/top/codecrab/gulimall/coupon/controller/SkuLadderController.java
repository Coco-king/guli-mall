package top.codecrab.gulimall.coupon.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.R;
import top.codecrab.gulimall.coupon.entity.SkuLadderEntity;
import top.codecrab.gulimall.coupon.service.SkuLadderService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 商品阶梯价格
 *
 * @author codecrab
 * @date 2021-05-28 22:26:50
 */
@RestController
@RequestMapping("coupon/skuladder")
public class SkuLadderController {

    @Resource
    private SkuLadderService skuLadderService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = skuLadderService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SkuLadderEntity skuLadder = skuLadderService.getById(id);

        return R.ok().put("skuLadder", skuLadder);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody SkuLadderEntity skuLadder) {
        skuLadderService.save(skuLadder);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody SkuLadderEntity skuLadder) {
        skuLadderService.updateById(skuLadder);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        skuLadderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
