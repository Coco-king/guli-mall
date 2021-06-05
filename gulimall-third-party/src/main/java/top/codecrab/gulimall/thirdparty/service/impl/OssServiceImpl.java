package top.codecrab.gulimall.thirdparty.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import org.springframework.stereotype.Service;
import top.codecrab.gulimall.thirdparty.config.OssProperties;
import top.codecrab.gulimall.thirdparty.service.OssService;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author codecrab
 * @since 2021年04月26日 17:39
 */
@Service
public class OssServiceImpl implements OssService {

    @Override
    public String upload(InputStream inputStream, String module, String fileName) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(
                OssProperties.ENDPOINT,
                OssProperties.KEY_ID,
                OssProperties.KEY_SECRET
        );

        // 判断桶是否存在
        if (!ossClient.doesBucketExist(OssProperties.BUCKET_NAME)) {
            // 不存在则创建桶
            ossClient.createBucket(OssProperties.BUCKET_NAME);
            // 授权
            ossClient.setBucketAcl(OssProperties.BUCKET_NAME, CannedAccessControlList.PublicRead);
        }

        // 组装文件路径  avatar/2021/04/26/fileName.jpg
        String filePath = module +
                DateUtil.format(LocalDateTime.now(), "/yyyy/MM/dd/") +
                IdUtil.fastSimpleUUID() + "." +
                FileUtil.getSuffix(fileName);

        // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
        ossClient.putObject(OssProperties.BUCKET_NAME, filePath, inputStream);
        // 关闭OSSClient。
        ossClient.shutdown();
        // 阿里云文件绝对路径
        return StrUtil.format("https://{}.{}/{}", OssProperties.BUCKET_NAME, OssProperties.ENDPOINT, filePath);
    }

    @Override
    public void removeFile(List<String> urls) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(
                OssProperties.ENDPOINT,
                OssProperties.KEY_ID,
                OssProperties.KEY_SECRET
        );

        try {
            // 需要截断的：https://srb-service-file.oss-cn-beijing.aliyuncs.com/
            String host = StrUtil.format("https://{}.{}/", OssProperties.BUCKET_NAME, OssProperties.ENDPOINT);
            urls.forEach(url -> {
                if (StrUtil.isNotBlank(url)) {
                    // 需要的：avatar/2021/04/26/8b40dde3d8ea4a2e8303fa3b279b8daa.jpg
                    String objectName = url.substring(host.length());

                    ossClient.deleteObject(OssProperties.BUCKET_NAME, objectName);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
    }

    @Override
    public Map<String, String> createPolicy(String type) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(
                OssProperties.ENDPOINT,
                OssProperties.KEY_ID,
                OssProperties.KEY_SECRET
        );

        String host = "https://" + OssProperties.BUCKET_NAME + "." + OssProperties.ENDPOINT;
        // callbackUrl为 上传回调服务器的URL，请将下面的IP和Port配置为您自己的真实信息。
        //String callbackUrl = "http://88.88.88.88:8888";
        String dir = type + new SimpleDateFormat("/yyyy/MM/").format(new Date());

        Map<String, String> respMap = null;
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyCons = new PolicyConditions();
            policyCons.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyCons.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyCons);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            respMap = new LinkedHashMap<>();
            respMap.put("accessId", OssProperties.KEY_ID);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            // respMap.put("expire", formatISO8601Date(expiration));
        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            System.out.println(e.getMessage());
        } finally {
            ossClient.shutdown();
        }
        return respMap;
    }
}
