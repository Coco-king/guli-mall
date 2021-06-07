package top.codecrab.gulimall.product.controller;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.web.bind.annotation.*;
import top.codecrab.common.response.R;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.gulimall.product.entity.AttrAttrgroupRelationEntity;
import top.codecrab.gulimall.product.entity.AttrEntity;
import top.codecrab.gulimall.product.entity.AttrGroupEntity;
import top.codecrab.gulimall.product.service.AttrAttrgroupRelationService;
import top.codecrab.gulimall.product.service.AttrGroupService;
import top.codecrab.gulimall.product.service.AttrService;
import top.codecrab.gulimall.product.service.CategoryService;
import top.codecrab.gulimall.product.vo.AttrGroupWithAttrsVo;
import top.codecrab.gulimall.product.vo.AttrRelationVo;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Resource
    private AttrAttrgroupRelationService relationService;

    /**
     * 列表
     */
    @GetMapping("/list/{categoryId}")
    public R list(
            @RequestParam Map<String, Object> params,
            @PathVariable Long categoryId
    ) {
        PageUtils page = attrGroupService.queryPage(params, categoryId);

        return R.ok().put("page", page);
    }

    /**
     * 根据分组ID查询出关联的基本属性
     */
    @GetMapping("/{attrGroupId}/attr/relation")
    public R attrRelation(@PathVariable Long attrGroupId) {
        List<AttrEntity> entities = attrService.findAttrRelation(attrGroupId);

        return R.ok().put("data", entities);
    }

    /**
     * 根据分组ID查询出未关联的基本属性
     */
    @GetMapping("/{attrGroupId}/noattr/relation")
    public R attrNoRelation(
            @RequestParam Map<String, Object> params,
            @PathVariable Long attrGroupId
    ) {
        PageUtils page = attrService.findAttrNoRelation(params, attrGroupId);

        return R.ok().put("page", page);
    }

    /**
     * 根据分类ID查询出关联的属性分组和分组下的属性
     */
    @GetMapping("/{catelogId}/withattr")
    public R attrGroupWithAttr(@PathVariable Long catelogId) {
        List<AttrGroupWithAttrsVo> vos = attrGroupService.getAttrGroupWithAttrByCatelogId(catelogId);

        return R.ok().put("data", vos);
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
     * 保存新的关联信息
     */
    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrRelationVo> vos) {

        List<AttrAttrgroupRelationEntity> relationEntities = vos.stream()
                .map(item -> BeanUtil.copyProperties(item, AttrAttrgroupRelationEntity.class))
                .collect(Collectors.toList());

        relationService.saveBatch(relationEntities);

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
