package top.codecrab.gulimall.ware.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.response.R;
import top.codecrab.gulimall.ware.entity.WareInfoEntity;
import top.codecrab.gulimall.ware.service.WareInfoService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 仓库信息
 *
 * @author codecrab
 * @date 2021-05-28 22:50:44
 */
@RestController
@RequestMapping("ware/wareinfo")
public class WareInfoController {

    @Resource
    private WareInfoService wareInfoService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = wareInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        WareInfoEntity wareInfo = wareInfoService.getById(id);

        return R.ok().put("wareInfo", wareInfo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody WareInfoEntity wareInfo) {
        wareInfoService.save(wareInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody WareInfoEntity wareInfo) {
        wareInfoService.updateById(wareInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        wareInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
