package top.codecrab.gulimall.order.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.response.R;
import top.codecrab.gulimall.order.entity.RefundInfoEntity;
import top.codecrab.gulimall.order.service.RefundInfoService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 退款信息
 *
 * @author codecrab
 * @date 2021-05-28 22:46:28
 */
@RestController
@RequestMapping("order/refundinfo")
public class RefundInfoController {

    @Resource
    private RefundInfoService refundInfoService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = refundInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        RefundInfoEntity refundInfo = refundInfoService.getById(id);

        return R.ok().put("refundInfo", refundInfo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody RefundInfoEntity refundInfo) {
        refundInfoService.save(refundInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody RefundInfoEntity refundInfo) {
        refundInfoService.updateById(refundInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        refundInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
