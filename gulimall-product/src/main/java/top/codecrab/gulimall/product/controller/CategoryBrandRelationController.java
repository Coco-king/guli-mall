package top.codecrab.gulimall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;
import top.codecrab.common.response.R;
import top.codecrab.gulimall.product.entity.BrandEntity;
import top.codecrab.gulimall.product.entity.CategoryBrandRelationEntity;
import top.codecrab.gulimall.product.service.CategoryBrandRelationService;
import top.codecrab.gulimall.product.vo.BrandVo;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 品牌分类关联
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {

    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 列表
     */
    @GetMapping("/catelog/list")
    public R catelogList(@RequestParam Long brandId) {
        List<CategoryBrandRelationEntity> list = categoryBrandRelationService.list(
                new QueryWrapper<CategoryBrandRelationEntity>()
                        .eq("brand_id", brandId)
        );

        return R.ok().put("data", list);
    }

    /**
     * 列表
     */
    @GetMapping("/brands/list")
    public R relationBrandsList(@RequestParam Long catId) {
        List<BrandEntity> entities = categoryBrandRelationService.getBrandsByCatId(catId);

        List<BrandVo> brandVos = entities.stream().map(brand -> {
            BrandVo vo = new BrandVo();
            vo.setBrandId(brand.getBrandId());
            vo.setBrandName(brand.getName());
            return vo;
        }).collect(Collectors.toList());

        return R.ok().put("data", brandVos);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.saveDetail(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
