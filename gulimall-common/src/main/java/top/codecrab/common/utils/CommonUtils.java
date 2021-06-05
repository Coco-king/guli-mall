package top.codecrab.common.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author codecrab
 */
public class CommonUtils {

    /**
     * 获取客户端真实IP
     */
    public static String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * 获取一言的sign
     */
    public static String getSign() {
        String url = "https://v1.hitokoto.cn?encode=json&charset=utf-8&c=a&c=b&c=d&c=e&c=f&c=g&c=h&c=i&c=j&c=k&c=l&c=c";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response =
                restTemplate.exchange(URI.create(url), HttpMethod.GET, new HttpEntity<>(null), String.class);
        return MapUtil.getStr(JSONUtil.toBean(response.getBody(), Map.class), "hitokoto");
    }

    /**
     * 把以obj形式存在的list对象转为list
     *
     * @param obj   list
     * @param clazz list中的对象类型字节码
     * @param <T>   对象类型
     * @return list集合
     */
    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

    /**
     * 把BindingResult的所有字段错误信息封装为字符串
     *
     * @param result 错误信息
     * @return 错误信息字符串
     */
    public static String getErrors(BindingResult result) {
        List<FieldError> errors = result.getFieldErrors();
        if (errors.size() == 1) {
            return errors.get(0).getDefaultMessage();
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < errors.size(); i++) {
            FieldError error = errors.get(i);
            if (i != errors.size() - 1) {
                // sb.append(error.getPropertyPath()).append(":").append(error.getMessage()).append(" ");
                sb.append("<p style='margin-bottom: 5px;'>")
                        .append(i + 1).append("、")
                        .append(error.getDefaultMessage())
                        .append("</p>");
            } else {
                sb.append("<p>").append(i + 1).append("、")
                        .append(error.getDefaultMessage())
                        .append("</p>");
            }
        }
        return sb.toString();
    }
}
