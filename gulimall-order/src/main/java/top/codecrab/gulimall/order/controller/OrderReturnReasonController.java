package top.codecrab.gulimall.order.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.R;
import top.codecrab.gulimall.order.entity.OrderReturnReasonEntity;
import top.codecrab.gulimall.order.service.OrderReturnReasonService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 退货原因
 *
 * @author codecrab
 * @date 2021-05-28 22:46:28
 */
@RestController
@RequestMapping("order/orderreturnreason")
public class OrderReturnReasonController {

    @Resource
    private OrderReturnReasonService orderReturnReasonService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = orderReturnReasonService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        OrderReturnReasonEntity orderReturnReason = orderReturnReasonService.getById(id);

        return R.ok().put("orderReturnReason", orderReturnReason);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody OrderReturnReasonEntity orderReturnReason) {
        orderReturnReasonService.save(orderReturnReason);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody OrderReturnReasonEntity orderReturnReason) {
        orderReturnReasonService.updateById(orderReturnReason);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        orderReturnReasonService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
