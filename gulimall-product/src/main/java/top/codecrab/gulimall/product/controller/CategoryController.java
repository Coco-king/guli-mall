package top.codecrab.gulimall.product.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.response.ResponseEnum;
import top.codecrab.common.utils.Assert;
import top.codecrab.common.response.R;
import top.codecrab.gulimall.product.entity.CategoryEntity;
import top.codecrab.gulimall.product.service.CategoryService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 商品三级分类
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 查询所有分类的树形列表
     */
    @GetMapping("/list/tree")
    public R list() {
        List<CategoryEntity> entities = categoryService.listWithTree();

        return R.ok().put("data", entities);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId) {
        CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody CategoryEntity category) {
        Assert.notBlank(category.getName(), ResponseEnum.CATEGORY_NAME_NULL_ERROR);

        categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody CategoryEntity category) {
        categoryService.updateById(category);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] catIds) {
        categoryService.removeMenuByIds(Arrays.asList(catIds));

        return R.ok();
    }

}
