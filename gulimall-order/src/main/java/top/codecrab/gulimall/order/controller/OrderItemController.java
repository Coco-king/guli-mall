package top.codecrab.gulimall.order.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.R;
import top.codecrab.gulimall.order.entity.OrderItemEntity;
import top.codecrab.gulimall.order.service.OrderItemService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 订单项信息
 *
 * @author codecrab
 * @date 2021-05-28 22:46:28
 */
@RestController
@RequestMapping("order/orderitem")
public class OrderItemController {

    @Resource
    private OrderItemService orderItemService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = orderItemService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        OrderItemEntity orderItem = orderItemService.getById(id);

        return R.ok().put("orderItem", orderItem);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody OrderItemEntity orderItem) {
        orderItemService.save(orderItem);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody OrderItemEntity orderItem) {
        orderItemService.updateById(orderItem);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        orderItemService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
