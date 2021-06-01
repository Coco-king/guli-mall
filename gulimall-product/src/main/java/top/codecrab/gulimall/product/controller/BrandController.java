package top.codecrab.gulimall.product.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.response.R;
import top.codecrab.gulimall.product.entity.BrandEntity;
import top.codecrab.gulimall.product.service.BrandService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 品牌
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {

    @Resource
    private BrandService brandService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId) {
        BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody BrandEntity brand) {
        brandService.save(brand);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody BrandEntity brand) {
        brandService.updateById(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] brandIds) {
        brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
