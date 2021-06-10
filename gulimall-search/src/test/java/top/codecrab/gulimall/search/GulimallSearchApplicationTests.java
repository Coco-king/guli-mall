package top.codecrab.gulimall.search;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.codecrab.gulimall.search.config.ElasticsearchConfig;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest
class GulimallSearchApplicationTests {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Test
    void contextLoads() {
        System.out.println(restHighLevelClient);
    }

    /**
     * 添加和更新操作
     */
    @Test
    void indexData() throws IOException {
        //创建一个请求，指定索引，索引不存在则自动创建
        IndexRequest indexRequest = new IndexRequest("users");
        //设置ID
        indexRequest.id("1");

        //可以使用键值对形式保存，不方便
        // indexRequest.source("username", "Jack", "gender", "男", "age", 18);

        //初始化一个对象
        User user = new User();
        user.setUsername("Lucy");
        user.setGender("女");
        user.setAge(18);

        //使用Json格式数据保存，保存时指定数据类型为Json格式
        String jsonStr = JSONUtil.toJsonStr(user);
        indexRequest.source(jsonStr, XContentType.JSON);

        //发送请求
        IndexResponse response = restHighLevelClient.index(indexRequest, ElasticsearchConfig.COMMON_OPTIONS);
        System.out.println(response);
    }

    @Test
    void searchData() throws IOException {
        //创建一个搜索请求
        SearchRequest searchRequest = new SearchRequest("newbank");
        //初始化一个搜索构建器
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //查出所有年龄段，并计算每个年龄段的男女工资平均值和总工资平均值
        //查询设置索引的所有记录
        sourceBuilder.query(QueryBuilders.matchAllQuery());

        //添加第一个聚合
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg").field("age").size(100);
        //添加ageAgg聚合的第一个子聚合
        AvgAggregationBuilder allBalanceAvg = AggregationBuilders.avg("allBalanceAvg").field("balance");
        ageAgg.subAggregation(allBalanceAvg);
        //添加ageAgg聚合的第二个子聚合
        TermsAggregationBuilder genderAgg = AggregationBuilders.terms("genderAgg").field("gender").size(5);
        ageAgg.subAggregation(genderAgg);
        //添加genderAgg聚合的第一个子聚合
        AvgAggregationBuilder balanceAvg = AggregationBuilders.avg("balanceAvg").field("balance");
        genderAgg.subAggregation(balanceAvg);

        //把总聚合传入sourceBuilder
        sourceBuilder.aggregation(ageAgg);
        sourceBuilder.sort("age", SortOrder.ASC);
        System.out.println(sourceBuilder);

        //指定搜索条件
        searchRequest.source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest, ElasticsearchConfig.COMMON_OPTIONS);
        System.out.println(response);

        //解析数据
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            String source = hit.getSourceAsString();
            Account account = JSONUtil.toBean(source, Account.class);
            System.out.println(account);
        }

        //解析聚合信息
        Aggregations aggregations = response.getAggregations();
        //此处用他的父接口接收
        Terms terms = aggregations.get("ageAgg");
        for (Terms.Bucket bucket : terms.getBuckets()) {
            String keyAsString = bucket.getKeyAsString();
            System.out.println("年龄：" + keyAsString);
            //获取子聚合
            Aggregations a = bucket.getAggregations();
            Avg allBalanceAvg1 = a.get("allBalanceAvg");
            System.out.println("\t总薪资平均值：" + allBalanceAvg1.getValue());

            Terms genderAgg1 = a.get("genderAgg");
            for (Terms.Bucket b : genderAgg1.getBuckets()) {
                String keyAsString1 = b.getKeyAsString().equals("M") ? "男性" : "女性";
                System.out.println("\t\t" + keyAsString1 + "：");
                Avg balanceAvg1 = b.getAggregations().get("balanceAvg");
                System.out.println("\t\t\t" + keyAsString1 + "的平均薪资：" + balanceAvg1.getValue());
            }
            System.out.println("================================================");
        }
    }

    @Data
    static class User {
        private String username;
        private String gender;
        private Integer age;
    }

    @NoArgsConstructor
    @Data
    static class Account {
        private Long accountNumber;
        private Integer balance;
        private String firstname;
        private String lastname;
        private Integer age;
        private String gender;
        private String address;
        private String employer;
        private String email;
        private String city;
        private String state;
    }

}
