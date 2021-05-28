package top.codecrab.gulimall.order.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.R;
import top.codecrab.gulimall.order.entity.PaymentInfoEntity;
import top.codecrab.gulimall.order.service.PaymentInfoService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 支付信息表
 *
 * @author codecrab
 * @date 2021-05-28 22:46:28
 */
@RestController
@RequestMapping("order/paymentinfo")
public class PaymentInfoController {

    @Resource
    private PaymentInfoService paymentInfoService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = paymentInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        PaymentInfoEntity paymentInfo = paymentInfoService.getById(id);

        return R.ok().put("paymentInfo", paymentInfo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody PaymentInfoEntity paymentInfo) {
        paymentInfoService.save(paymentInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody PaymentInfoEntity paymentInfo) {
        paymentInfoService.updateById(paymentInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        paymentInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
