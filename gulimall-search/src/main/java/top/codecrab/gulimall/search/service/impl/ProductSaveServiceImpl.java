package top.codecrab.gulimall.search.service.impl;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;
import top.codecrab.common.to.es.SkuEsModel;
import top.codecrab.gulimall.search.config.ElasticsearchConfig;
import top.codecrab.gulimall.search.constant.ElasticConstant;
import top.codecrab.gulimall.search.service.ProductSaveService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author codecrab
 * @since 2021年06月10日 20:16
 */
@Slf4j
@Service
public class ProductSaveServiceImpl implements ProductSaveService {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Override
    public boolean produceStatusUp(List<SkuEsModel> modelList) throws IOException {

        BulkRequest bulkRequest = new BulkRequest();
        modelList.forEach(skuEsModel -> {
            IndexRequest indexRequest = new IndexRequest(ElasticConstant.PRODUCT_INDEX);
            indexRequest.id(skuEsModel.getSkuId().toString());

            String jsonStr = JSONUtil.toJsonStr(skuEsModel);
            indexRequest.source(jsonStr, XContentType.JSON);
            bulkRequest.add(indexRequest);
        });

        BulkResponse response = restHighLevelClient.bulk(bulkRequest, ElasticsearchConfig.COMMON_OPTIONS);
        boolean failures = response.hasFailures();
        List<String> collect = Arrays.stream(response.getItems())
                .map(BulkItemResponse::getId)
                .collect(Collectors.toList());
        if (failures) {
            log.error("商品上架异常：{}\n异常信息：{}", collect, response.buildFailureMessage());
            return false;
        } else {
            log.info("商品上架完成；{}", collect);
            return true;
        }
    }
}
