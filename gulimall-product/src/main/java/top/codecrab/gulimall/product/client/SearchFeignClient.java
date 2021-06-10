package top.codecrab.gulimall.product.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.codecrab.common.response.R;
import top.codecrab.common.to.es.SkuEsModel;

import java.util.List;

/**
 * @author codecrab
 * @since 2021年05月29日 13:41
 */
@FeignClient("gulimall-search")
public interface SearchFeignClient {

    @PostMapping("/search/save/product")
    R produceStatusUp(@RequestBody List<SkuEsModel> modelList);
}
