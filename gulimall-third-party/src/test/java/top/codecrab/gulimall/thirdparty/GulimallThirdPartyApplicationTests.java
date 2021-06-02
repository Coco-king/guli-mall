package top.codecrab.gulimall.thirdparty;

import com.aliyun.oss.OSS;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
class GulimallThirdPartyApplicationTests {

    @Resource
    private OSS ossClient;

    @Value("${spring.cloud.alicloud.oss.bucket-name}")
    private String bucketName;

    @Test
    void testUpload() throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream("D:\\JetBrains\\image\\tx.jpg");
        ossClient.putObject(bucketName, "haha.jpg", inputStream);
        ossClient.shutdown();
    }

}
