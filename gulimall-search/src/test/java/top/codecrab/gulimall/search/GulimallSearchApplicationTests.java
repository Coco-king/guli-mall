package top.codecrab.gulimall.search;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
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

    @Data
    static class User {
        private String username;
        private String gender;
        private Integer age;
    }

}
