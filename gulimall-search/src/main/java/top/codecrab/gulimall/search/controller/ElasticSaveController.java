package top.codecrab.gulimall.search.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.codecrab.common.response.ErrorCodeEnum;
import top.codecrab.common.response.R;
import top.codecrab.common.to.es.SkuEsModel;
import top.codecrab.gulimall.search.service.ProductSaveService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author codecrab
 * @since 2021年06月10日 20:13
 */
@Slf4j
@RestController
@RequestMapping("/search/save")
public class ElasticSaveController {

    @Resource
    private ProductSaveService productSaveService;

    @PostMapping("/product")
    public R produceStatusUp(@RequestBody List<SkuEsModel> modelList) {

        boolean isSuccess;
        try {
            isSuccess = productSaveService.produceStatusUp(modelList);
        } catch (IOException e) {
            log.error("========== 商品上架异常 ==========", e);
            return R.error(ErrorCodeEnum.PRODUCT_UP_ERROR);
        }

        return isSuccess ? R.ok() : R.error(ErrorCodeEnum.PRODUCT_UP_ERROR);
    }

}
