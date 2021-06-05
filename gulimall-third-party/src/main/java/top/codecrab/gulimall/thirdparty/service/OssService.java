package top.codecrab.gulimall.thirdparty.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author codecrab
 * @since 2021年04月26日 17:37
 */
public interface OssService {

    /**
     * 把文件上传到阿里云OSS
     *
     * @param inputStream 文件流
     * @param module      一级目录
     * @param fileName    原始文件名
     * @return 上传成功的路径
     */
    String upload(InputStream inputStream, String module, String fileName);

    /**
     * 删除阿里云OSS文件
     *
     * @param urls 要删除的文件路径集合
     */
    void removeFile(List<String> urls);

    Map<String, String> createPolicy(String type);
}
