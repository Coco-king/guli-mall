package top.codecrab.gulimall.member.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.R;
import top.codecrab.gulimall.member.entity.MemberLevelEntity;
import top.codecrab.gulimall.member.service.MemberLevelService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 会员等级
 *
 * @author codecrab
 * @date 2021-05-28 22:40:42
 */
@RestController
@RequestMapping("member/memberlevel")
public class MemberLevelController {

    @Resource
    private MemberLevelService memberLevelService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberLevelService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        MemberLevelEntity memberLevel = memberLevelService.getById(id);

        return R.ok().put("memberLevel", memberLevel);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody MemberLevelEntity memberLevel) {
        memberLevelService.save(memberLevel);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody MemberLevelEntity memberLevel) {
        memberLevelService.updateById(memberLevel);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        memberLevelService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
