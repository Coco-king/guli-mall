package top.codecrab.gulimall.product.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.response.R;
import top.codecrab.gulimall.product.entity.SpuInfoEntity;
import top.codecrab.gulimall.product.service.SpuInfoService;
import top.codecrab.gulimall.product.vo.spu.SpuSaveVo;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * spu信息
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@RestController
@RequestMapping("product/spuinfo")
public class SpuInfoController {

    @Resource
    private SpuInfoService spuInfoService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = spuInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存
     */
    @PostMapping("/{spuId}/up")
    public R up(@PathVariable Long spuId) {
        spuInfoService.up(spuId);

        return R.ok();
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody SpuSaveVo saveVo) {
        spuInfoService.saveSpuInfo(saveVo);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody SpuInfoEntity spuInfo) {
        spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
