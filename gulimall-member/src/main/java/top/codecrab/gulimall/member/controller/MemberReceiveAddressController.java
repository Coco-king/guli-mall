package top.codecrab.gulimall.member.controller;

import org.springframework.web.bind.annotation.*;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.R;
import top.codecrab.gulimall.member.entity.MemberReceiveAddressEntity;
import top.codecrab.gulimall.member.service.MemberReceiveAddressService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 会员收货地址
 *
 * @author codecrab
 * @date 2021-05-28 22:40:42
 */
@RestController
@RequestMapping("member/memberreceiveaddress")
public class MemberReceiveAddressController {

    @Resource
    private MemberReceiveAddressService memberReceiveAddressService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberReceiveAddressService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        MemberReceiveAddressEntity memberReceiveAddress = memberReceiveAddressService.getById(id);

        return R.ok().put("memberReceiveAddress", memberReceiveAddress);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody MemberReceiveAddressEntity memberReceiveAddress) {
        memberReceiveAddressService.save(memberReceiveAddress);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody MemberReceiveAddressEntity memberReceiveAddress) {
        memberReceiveAddressService.updateById(memberReceiveAddress);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        memberReceiveAddressService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
