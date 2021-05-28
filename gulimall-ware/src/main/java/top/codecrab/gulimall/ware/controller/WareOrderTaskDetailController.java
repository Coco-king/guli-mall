package top.codecrab.gulimall.ware.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.R;
import top.codecrab.gulimall.ware.entity.WareOrderTaskDetailEntity;
import top.codecrab.gulimall.ware.service.WareOrderTaskDetailService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 库存工作单
 *
 * @author codecrab
 * @date 2021-05-28 22:50:44
 */
@RestController
@RequestMapping("ware/wareordertaskdetail")
public class WareOrderTaskDetailController {

    @Resource
    private WareOrderTaskDetailService wareOrderTaskDetailService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = wareOrderTaskDetailService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        WareOrderTaskDetailEntity wareOrderTaskDetail = wareOrderTaskDetailService.getById(id);

        return R.ok().put("wareOrderTaskDetail", wareOrderTaskDetail);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody WareOrderTaskDetailEntity wareOrderTaskDetail) {
        wareOrderTaskDetailService.save(wareOrderTaskDetail);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody WareOrderTaskDetailEntity wareOrderTaskDetail) {
        wareOrderTaskDetailService.updateById(wareOrderTaskDetail);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        wareOrderTaskDetailService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
