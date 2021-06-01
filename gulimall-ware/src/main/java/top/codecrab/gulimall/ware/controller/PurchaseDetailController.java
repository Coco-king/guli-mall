package top.codecrab.gulimall.ware.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.response.R;
import top.codecrab.gulimall.ware.entity.PurchaseDetailEntity;
import top.codecrab.gulimall.ware.service.PurchaseDetailService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 *
 *
 * @author codecrab
 * @date 2021-05-28 22:50:44
 */
@RestController
@RequestMapping("ware/purchasedetail")
public class PurchaseDetailController {

    @Resource
    private PurchaseDetailService purchaseDetailService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseDetailService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        PurchaseDetailEntity purchaseDetail = purchaseDetailService.getById(id);

        return R.ok().put("purchaseDetail", purchaseDetail);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody PurchaseDetailEntity purchaseDetail) {
        purchaseDetailService.save(purchaseDetail);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody PurchaseDetailEntity purchaseDetail) {
        purchaseDetailService.updateById(purchaseDetail);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        purchaseDetailService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
