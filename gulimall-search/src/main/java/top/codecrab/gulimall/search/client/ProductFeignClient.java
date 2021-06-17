package top.codecrab.gulimall.search.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import top.codecrab.common.response.R;

import java.util.List;

/**
 * @author codecrab
 * @since 2021年06月17日 11:30
 */
@FeignClient("gulimall-product")
public interface ProductFeignClient {

    @GetMapping("/product/attr/info/{attrId}")
    R attrInfo(@PathVariable("attrId") Long attrId);

    @GetMapping("/product/brand/infos")
    R brandInfos(@RequestParam("brandIds") List<Long> brandIds);

    @GetMapping("/product/category/info/{catId}")
    R categoryInfo(@PathVariable("catId") Long catId);
}
