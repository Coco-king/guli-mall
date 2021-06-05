package top.codecrab.gulimall.thirdparty.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.bind.annotation.*;
import top.codecrab.common.response.R;
import top.codecrab.common.response.ErrorCodeEnum;
import top.codecrab.common.utils.Assert;
import top.codecrab.gulimall.thirdparty.service.OssService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author codecrab
 * @since 2021年06月02日 8:47
 */
@RestController
public class OssController {

    @Resource
    private OssService ossService;

    @GetMapping("/oss/policy")
    public R policy(@RequestParam(required = false) String type) {
        if (StrUtil.isBlank(type)) {
            type = "default";
        }
        Map<String, String> respMap = ossService.createPolicy(type);
        return R.ok().put("data", respMap);
    }

    @DeleteMapping("/oss/remove")
    public R remove(@RequestBody Map<String, List<String>> params) {
        List<String> urls = params.get("urls");
        Assert.isFalse(CollectionUtil.isEmpty(urls), ErrorCodeEnum.REMOVE_FILE_URL_NULL_ERROR);

        ossService.removeFile(urls);
        return R.ok();
    }

}
