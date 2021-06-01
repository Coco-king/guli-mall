package top.codecrab.gulimall.member.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.response.R;
import top.codecrab.gulimall.member.entity.MemberCollectSubjectEntity;
import top.codecrab.gulimall.member.service.MemberCollectSubjectService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 会员收藏的专题活动
 *
 * @author codecrab
 * @date 2021-05-28 22:40:42
 */
@RestController
@RequestMapping("member/membercollectsubject")
public class MemberCollectSubjectController {

    @Resource
    private MemberCollectSubjectService memberCollectSubjectService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberCollectSubjectService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        MemberCollectSubjectEntity memberCollectSubject = memberCollectSubjectService.getById(id);

        return R.ok().put("memberCollectSubject", memberCollectSubject);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody MemberCollectSubjectEntity memberCollectSubject) {
        memberCollectSubjectService.save(memberCollectSubject);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody MemberCollectSubjectEntity memberCollectSubject) {
        memberCollectSubjectService.updateById(memberCollectSubject);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        memberCollectSubjectService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
