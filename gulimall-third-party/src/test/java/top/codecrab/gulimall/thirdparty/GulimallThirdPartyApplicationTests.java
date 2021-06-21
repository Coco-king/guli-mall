package top.codecrab.gulimall.thirdparty;

import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.codecrab.gulimall.thirdparty.config.OssProperties;
import top.codecrab.gulimall.thirdparty.config.SmsProperties;
import top.codecrab.gulimall.thirdparty.utils.SmsUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class GulimallThirdPartyApplicationTests {

    @Test
    void testUpload() throws FileNotFoundException {
        OSS ossClient = new OSSClientBuilder().build(
                OssProperties.ENDPOINT,
                OssProperties.KEY_ID,
                OssProperties.KEY_SECRET
        );

        FileInputStream inputStream = new FileInputStream("D:\\JetBrains\\image\\tx.jpg");
        ossClient.putObject(OssProperties.BUCKET_NAME, "haha.jpg", inputStream);
        ossClient.shutdown();
    }

    @Test
    void testSendCodeByAppCode() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("code", "111111");
        map.put("minute", "5");
        HttpResponse response = SmsUtils.sendSmsByAppCode("17122840624", SmsProperties.TEMPLATE_ID, map);
        System.out.println(response.getStatusLine());
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    @Test
    void testSendCodeByAccess() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("code", "111111");
        SendSmsResponse response = SmsUtils.sendSmsByAccess("17122840624", SmsProperties.TEMPLATE_CODE, map);
        System.out.println(new Gson().toJson(response));
    }

}
