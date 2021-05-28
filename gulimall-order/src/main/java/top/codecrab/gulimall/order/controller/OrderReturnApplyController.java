package top.codecrab.gulimall.order.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.R;
import top.codecrab.gulimall.order.entity.OrderReturnApplyEntity;
import top.codecrab.gulimall.order.service.OrderReturnApplyService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 订单退货申请
 *
 * @author codecrab
 * @date 2021-05-28 22:46:28
 */
@RestController
@RequestMapping("order/orderreturnapply")
public class OrderReturnApplyController {

    @Resource
    private OrderReturnApplyService orderReturnApplyService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = orderReturnApplyService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        OrderReturnApplyEntity orderReturnApply = orderReturnApplyService.getById(id);

        return R.ok().put("orderReturnApply", orderReturnApply);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody OrderReturnApplyEntity orderReturnApply) {
        orderReturnApplyService.save(orderReturnApply);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody OrderReturnApplyEntity orderReturnApply) {
        orderReturnApplyService.updateById(orderReturnApply);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        orderReturnApplyService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
