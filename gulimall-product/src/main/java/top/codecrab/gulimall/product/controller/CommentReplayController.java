package top.codecrab.gulimall.product.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.response.R;
import top.codecrab.gulimall.product.entity.CommentReplayEntity;
import top.codecrab.gulimall.product.service.CommentReplayService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 商品评价回复关系
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@RestController
@RequestMapping("product/commentreplay")
public class CommentReplayController {

    @Resource
    private CommentReplayService commentReplayService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = commentReplayService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        CommentReplayEntity commentReplay = commentReplayService.getById(id);

        return R.ok().put("commentReplay", commentReplay);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody CommentReplayEntity commentReplay) {
        commentReplayService.save(commentReplay);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody CommentReplayEntity commentReplay) {
        commentReplayService.updateById(commentReplay);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        commentReplayService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
