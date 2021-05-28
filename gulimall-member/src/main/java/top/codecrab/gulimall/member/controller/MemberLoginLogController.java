package top.codecrab.gulimall.member.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.R;
import top.codecrab.gulimall.member.entity.MemberLoginLogEntity;
import top.codecrab.gulimall.member.service.MemberLoginLogService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 会员登录记录
 *
 * @author codecrab
 * @date 2021-05-28 22:40:42
 */
@RestController
@RequestMapping("member/memberloginlog")
public class MemberLoginLogController {

    @Resource
    private MemberLoginLogService memberLoginLogService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberLoginLogService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        MemberLoginLogEntity memberLoginLog = memberLoginLogService.getById(id);

        return R.ok().put("memberLoginLog", memberLoginLog);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody MemberLoginLogEntity memberLoginLog) {
        memberLoginLogService.save(memberLoginLog);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody MemberLoginLogEntity memberLoginLog) {
        memberLoginLogService.updateById(memberLoginLog);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        memberLoginLogService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
