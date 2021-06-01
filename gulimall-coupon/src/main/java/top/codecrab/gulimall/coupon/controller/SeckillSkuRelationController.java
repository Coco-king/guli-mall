package top.codecrab.gulimall.coupon.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.response.R;
import top.codecrab.gulimall.coupon.entity.SeckillSkuRelationEntity;
import top.codecrab.gulimall.coupon.service.SeckillSkuRelationService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 秒杀活动商品关联
 *
 * @author codecrab
 * @date 2021-05-28 22:26:50
 */
@RestController
@RequestMapping("coupon/seckillskurelation")
public class SeckillSkuRelationController {

    @Resource
    private SeckillSkuRelationService seckillSkuRelationService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = seckillSkuRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SeckillSkuRelationEntity seckillSkuRelation = seckillSkuRelationService.getById(id);

        return R.ok().put("seckillSkuRelation", seckillSkuRelation);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody SeckillSkuRelationEntity seckillSkuRelation) {
        seckillSkuRelationService.save(seckillSkuRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody SeckillSkuRelationEntity seckillSkuRelation) {
        seckillSkuRelationService.updateById(seckillSkuRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        seckillSkuRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
