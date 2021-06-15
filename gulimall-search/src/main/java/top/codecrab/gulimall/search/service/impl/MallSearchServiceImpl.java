package top.codecrab.gulimall.search.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import top.codecrab.gulimall.search.config.ElasticsearchConfig;
import top.codecrab.gulimall.search.constant.ElasticConstant;
import top.codecrab.gulimall.search.service.MallSearchService;
import top.codecrab.gulimall.search.vo.SearchParam;
import top.codecrab.gulimall.search.vo.SearchResult;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author codecrab
 * @since 2021年06月15日 17:31
 */
@Service("mallSearchService")
public class MallSearchServiceImpl implements MallSearchService {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Override
    public SearchResult search(SearchParam param) {
        //构建搜索dsl
        SearchRequest request = buildSearchRequest(param);
        SearchResult result = null;
        //发送请求
        try {
            SearchResponse response = restHighLevelClient.search(request, ElasticsearchConfig.COMMON_OPTIONS);
            //构建返回对象
            result = buildSearchResult(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private SearchRequest buildSearchRequest(SearchParam param) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        if (StrUtil.isNotBlank(param.getKeyword())) {
            boolQuery.must(QueryBuilders.matchQuery("skuTitle", param.getKeyword()));
        }

        if (CollectionUtil.isNotEmpty(param.getBrandId())) {
            boolQuery.filter(QueryBuilders.termsQuery("brandId", param.getBrandId()));
        }

        if (param.getCatalogId() != null && param.getCatalogId() > 0) {
            boolQuery.filter(QueryBuilders.termQuery("catalogId", param.getCatalogId()));
        }

        if (CollectionUtil.isNotEmpty(param.getAttrs())) {
            // attrs=1_3G:4G
            param.getAttrs().forEach(attrStr -> {
                String[] split = attrStr.split("_");
                if (split.length == 2) {
                    String attrId = split[0];
                    String[] attrValues = split[1].split(":");
                    BoolQueryBuilder query = QueryBuilders.boolQuery();
                    query.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                    query.must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));
                    boolQuery.filter(QueryBuilders.nestedQuery("attrs", query, ScoreMode.None));
                }
            });
        }

        //price=0_400 或者 price=_200 或者 price=200_
        String price = param.getPrice();
        if (StrUtil.isNotBlank(price) && price.contains("_")) {
            if (price.startsWith("_")) {
                String max = price.substring(1);
                boolQuery.filter(QueryBuilders.rangeQuery("skuPrice").lte(max));
            } else if (price.endsWith("_")) {
                String min = price.substring(0, price.length() - 1);
                boolQuery.filter(QueryBuilders.rangeQuery("skuPrice").gte(min));
            } else {
                String[] s = price.split("_");
                if (s.length == 2) {
                    boolQuery.filter(QueryBuilders.rangeQuery("skuPrice").gte(s[0]).lte(s[1]));
                }
            }
        }

        boolQuery.filter(QueryBuilders.termQuery("hasStock", param.getHasStock() == 1));
        sourceBuilder.query(boolQuery);

        //hotScore_desc
        String sort = param.getSort();
        if (StrUtil.isNotBlank(sort)) {
            String[] split = sort.split("_");
            String field = split[0];
            String sortRule = split[1];
            if (SortOrder.ASC.toString().equalsIgnoreCase(sortRule)) {
                sourceBuilder.sort(field, SortOrder.ASC);
            } else if (SortOrder.DESC.toString().equalsIgnoreCase(sortRule)) {
                sourceBuilder.sort(field, SortOrder.DESC);
            }
        }

        Integer currPage = param.getCurrPage();
        if (currPage == null || currPage <= 0) {
            currPage = 1;
        }

        sourceBuilder.size(ElasticConstant.PRODUCT_PAGE_SIZE);
        sourceBuilder.from((currPage - 1) * ElasticConstant.PRODUCT_PAGE_SIZE);

        if (StrUtil.isNotBlank(param.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle").preTags("<b style='color:red'>").postTags("</b>");
            sourceBuilder.highlighter(highlightBuilder);
        }

        //================== 添加聚合条件 ==================
        TermsAggregationBuilder brandIdAgg = AggregationBuilders.terms("brand_id_agg").field("brandId").size(18);
        brandIdAgg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(5));
        brandIdAgg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(5));
        sourceBuilder.aggregation(brandIdAgg);

        TermsAggregationBuilder catalogIdAgg = AggregationBuilders.terms("catalog_id_agg").field("catalogId").size(20);
        catalogIdAgg.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(5));
        sourceBuilder.aggregation(catalogIdAgg);

        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("attr_agg", "attrs");
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId").size(50);
        attrIdAgg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(5));
        attrIdAgg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(5));
        attrAgg.subAggregation(attrIdAgg);
        sourceBuilder.aggregation(attrAgg);

        System.out.println(sourceBuilder.toString());
        return new SearchRequest(new String[]{ElasticConstant.PRODUCT_INDEX}, sourceBuilder);
    }

    private SearchResult buildSearchResult(SearchResponse response) {
        return null;
    }

}
