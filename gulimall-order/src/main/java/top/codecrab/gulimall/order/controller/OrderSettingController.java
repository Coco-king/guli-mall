package top.codecrab.gulimall.order.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.R;
import top.codecrab.gulimall.order.entity.OrderSettingEntity;
import top.codecrab.gulimall.order.service.OrderSettingService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 订单配置信息
 *
 * @author codecrab
 * @date 2021-05-28 22:46:28
 */
@RestController
@RequestMapping("order/ordersetting")
public class OrderSettingController {

    @Resource
    private OrderSettingService orderSettingService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = orderSettingService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        OrderSettingEntity orderSetting = orderSettingService.getById(id);

        return R.ok().put("orderSetting", orderSetting);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody OrderSettingEntity orderSetting) {
        orderSettingService.save(orderSetting);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody OrderSettingEntity orderSetting) {
        orderSettingService.updateById(orderSetting);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        orderSettingService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
