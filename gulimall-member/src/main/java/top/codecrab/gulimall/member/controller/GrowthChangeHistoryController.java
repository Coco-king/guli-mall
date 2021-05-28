package top.codecrab.gulimall.member.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.R;
import top.codecrab.gulimall.member.entity.GrowthChangeHistoryEntity;
import top.codecrab.gulimall.member.service.GrowthChangeHistoryService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 成长值变化历史记录
 *
 * @author codecrab
 * @date 2021-05-28 22:40:42
 */
@RestController
@RequestMapping("member/growthchangehistory")
public class GrowthChangeHistoryController {

    @Resource
    private GrowthChangeHistoryService growthChangeHistoryService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = growthChangeHistoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        GrowthChangeHistoryEntity growthChangeHistory = growthChangeHistoryService.getById(id);

        return R.ok().put("growthChangeHistory", growthChangeHistory);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody GrowthChangeHistoryEntity growthChangeHistory) {
        growthChangeHistoryService.save(growthChangeHistory);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody GrowthChangeHistoryEntity growthChangeHistory) {
        growthChangeHistoryService.updateById(growthChangeHistory);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        growthChangeHistoryService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
