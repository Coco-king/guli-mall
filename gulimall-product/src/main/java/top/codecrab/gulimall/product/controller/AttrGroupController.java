package top.codecrab.gulimall.product.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.response.R;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.gulimall.product.entity.AttrEntity;
import top.codecrab.gulimall.product.entity.AttrGroupEntity;
import top.codecrab.gulimall.product.service.AttrGroupService;
import top.codecrab.gulimall.product.service.AttrService;
import top.codecrab.gulimall.product.service.CategoryService;
import top.codecrab.gulimall.product.vo.AttrRelationVo;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {

    @Resource
    private AttrService attrService;

    @Resource
    private AttrGroupService attrGroupService;

    @Resource
    private CategoryService categoryService;

    /**
     * 列表
     */
    @GetMapping("/list/{categoryId}")
    public R list(@RequestParam Map<String, Object> params, @PathVariable Long categoryId) {
        PageUtils page = attrGroupService.queryPage(params, categoryId);

        return R.ok().put("page", page);
    }

    /**
     * 根据分组ID查询出关联的属性
     */
    @GetMapping("/{attrGroupId}/attr/relation")
    public R attrRelation(@PathVariable Long attrGroupId) {
        List<AttrEntity> entities = attrService.findAttrRelation(attrGroupId);

        return R.ok().put("data", entities);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        List<Long> catelogPath = categoryService.findCatelogPath(attrGroup.getCatelogId());
        attrGroup.setCatelogPath(catelogPath);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/attr/relation/delete")
    public R relationDelete(@RequestBody List<AttrRelationVo> relationVos) {
        attrService.removeRelations(relationVos);

        return R.ok();
    }

}
