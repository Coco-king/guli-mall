package top.codecrab.gulimall.ware.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.response.R;
import top.codecrab.common.to.SkuHasStockTo;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.gulimall.ware.entity.WareSkuEntity;
import top.codecrab.gulimall.ware.service.WareSkuService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author codecrab
 * @date 2021-05-28 22:50:44
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {

    @Resource
    private WareSkuService wareSkuService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 远程调用接口，查询是否有库存
     */
    @PostMapping("/has-stock")
    public R hasStock(@RequestBody List<Long> skuIds) {
        List<SkuHasStockTo> hasStock = wareSkuService.getSkusHasStock(skuIds);

        return R.ok().setFeignData(hasStock);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody WareSkuEntity wareSku) {
        wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody WareSkuEntity wareSku) {
        wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
