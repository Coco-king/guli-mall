package top.codecrab.gulimall.order.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.response.R;
import top.codecrab.gulimall.order.entity.OrderOperateHistoryEntity;
import top.codecrab.gulimall.order.service.OrderOperateHistoryService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 订单操作历史记录
 *
 * @author codecrab
 * @date 2021-05-28 22:46:28
 */
@RestController
@RequestMapping("order/orderoperatehistory")
public class OrderOperateHistoryController {

    @Resource
    private OrderOperateHistoryService orderOperateHistoryService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = orderOperateHistoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        OrderOperateHistoryEntity orderOperateHistory = orderOperateHistoryService.getById(id);

        return R.ok().put("orderOperateHistory", orderOperateHistory);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody OrderOperateHistoryEntity orderOperateHistory) {
        orderOperateHistoryService.save(orderOperateHistory);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody OrderOperateHistoryEntity orderOperateHistory) {
        orderOperateHistoryService.updateById(orderOperateHistory);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        orderOperateHistoryService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
