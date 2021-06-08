package top.codecrab.gulimall.ware.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.codecrab.common.response.R;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.gulimall.ware.entity.PurchaseEntity;
import top.codecrab.gulimall.ware.service.PurchaseService;
import top.codecrab.gulimall.ware.vo.MergeVo;
import top.codecrab.gulimall.ware.vo.PurchaseDoneVo;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author codecrab
 * @date 2021-05-28 22:50:44
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {

    @Resource
    private PurchaseService purchaseService;

    /**
     * 查询未领取的采购单
     */
    @GetMapping("/unreceive/list")
    public R unReceiveList(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPageUnReceiveList(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase) {
        purchase.setCreateTime(LocalDateTime.now());
        purchase.setUpdateTime(purchase.getCreateTime());
        purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 合并采购需求
     */
    @PostMapping("/merge")
    public R merge(@RequestBody MergeVo mergeVo) {
        purchaseService.merge(mergeVo);

        return R.ok();
    }

    /**
     * 领取采购单
     */
    @PostMapping("/received")
    public R received(@RequestBody List<Long> purchaseIds) {
        purchaseService.received(purchaseIds);

        return R.ok();
    }

    /**
     * 领取采购单
     */
    @PostMapping("/done")
    public R done(@Validated @RequestBody PurchaseDoneVo doneVo) {
        purchaseService.done(doneVo);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase) {
        purchase.setUpdateTime(LocalDateTime.now());
        purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
