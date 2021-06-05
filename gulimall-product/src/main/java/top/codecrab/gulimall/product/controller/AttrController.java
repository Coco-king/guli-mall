package top.codecrab.gulimall.product.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.response.R;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.gulimall.product.service.AttrService;
import top.codecrab.gulimall.product.vo.AttrResponseVo;
import top.codecrab.gulimall.product.vo.AttrVo;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 商品属性
 *
 * @author codecrab
 * @date 2021-05-28 22:19:47
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {

    @Resource
    private AttrService attrService;

    @GetMapping("/{attrType}/list/{catelogId}")
    private R baseList(
            @RequestParam Map<String, Object> params,
            @PathVariable Long catelogId,
            @PathVariable String attrType
    ) {
        PageUtils page = attrService.queryBaseList(params, catelogId, attrType);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId) {
        AttrResponseVo attr = attrService.getAttrResponseVo(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody AttrVo attr) {
        attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody AttrVo attr) {
        attrService.updateAttr(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] attrIds) {
        attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
