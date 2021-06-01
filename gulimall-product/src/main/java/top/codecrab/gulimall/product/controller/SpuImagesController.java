package top.codecrab.gulimall.product.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.response.R;
import top.codecrab.gulimall.product.entity.SpuImagesEntity;
import top.codecrab.gulimall.product.service.SpuImagesService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * spu图片
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@RestController
@RequestMapping("product/spuimages")
public class SpuImagesController {

    @Resource
    private SpuImagesService spuImagesService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = spuImagesService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SpuImagesEntity spuImages = spuImagesService.getById(id);

        return R.ok().put("spuImages", spuImages);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody SpuImagesEntity spuImages) {
        spuImagesService.save(spuImages);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody SpuImagesEntity spuImages) {
        spuImagesService.updateById(spuImages);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        spuImagesService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
