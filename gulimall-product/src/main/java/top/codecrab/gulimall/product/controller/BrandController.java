package top.codecrab.gulimall.product.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.codecrab.common.response.ErrorCodeEnum;
import top.codecrab.common.response.R;
import top.codecrab.common.utils.Assert;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.valid.AddGroup;
import top.codecrab.common.valid.UpdateGroup;
import top.codecrab.common.valid.UpdateStatusGroup;
import top.codecrab.gulimall.product.client.ThirdPartyFeignClient;
import top.codecrab.gulimall.product.entity.BrandEntity;
import top.codecrab.gulimall.product.service.BrandService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Resource
    private ThirdPartyFeignClient thirdPartyFeignClient;

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
    public R save(@Validated(AddGroup.class) @RequestBody BrandEntity brand) {
        if (StrUtil.isBlank(brand.getDescript())) {
            brand.setDescript(brand.getName() + "品牌");
        }
        if (StrUtil.isNotBlank(brand.getFirstLetter())) {
            brand.setFirstLetter(brand.getFirstLetter().toUpperCase());
        }
        brandService.save(brand);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@Validated(UpdateGroup.class) @RequestBody BrandEntity brand) {
        Assert.isNotAllEmpty(ErrorCodeEnum.VALID_NOT_ALL_NULL_EXCEPTION, brand.getName(), brand.getLogo(), brand.getFirstLetter(), brand.getShowStatus(), brand.getSort(), brand.getDescript());
        if (StrUtil.isNotBlank(brand.getFirstLetter())) {
            brand.setFirstLetter(brand.getFirstLetter().toUpperCase());
        }
        brandService.updateDetail(brand);

        return R.ok();
    }

    /**
     * 修改状态
     */
    @PutMapping("/update/status")
    public R updateStatus(@Validated(UpdateStatusGroup.class) @RequestBody BrandEntity brand) {
        brandService.updateById(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] brandIds) {
        List<Long> idList = Arrays.asList(brandIds);

        List<String> urls = brandService.listByIds(idList).stream()
                .map(BrandEntity::getLogo)
                .collect(Collectors.toList());
        thirdPartyFeignClient.remove(MapUtil.of("urls", urls));

        brandService.removeByIds(idList);

        return R.ok();
    }

}
