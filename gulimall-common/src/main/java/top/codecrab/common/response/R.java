package top.codecrab.common.response;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import org.apache.http.HttpStatus;
import top.codecrab.common.constant.FeignConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author Mark sunlightcs@gmail.com
 */
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public R() {
        put("code", 0);
        put("msg", "success");
    }

    public static R error() {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R error(ErrorCodeEnum codeEnum) {
        return error(codeEnum.getCode(), codeEnum.getMsg());
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public static R setResult(ErrorCodeEnum errorCodeEnum) {
        return error(errorCodeEnum.getCode(), errorCodeEnum.getMsg());
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public R data(String key, Object value) {
        this.put(key, value);
        return this;
    }

    public Integer getCode() {
        return (Integer) this.get("code");
    }

    public String getMsg() {
        return (String) this.get("msg");
    }

    public Object getFeignData() {
        return this.get(FeignConstant.FEIGN_RESP_KEY);
    }

    public <T> T getFeignData(TypeReference<T> typeReference) {
        Object feignData = this.getFeignData();
        String jsonStr = JSONUtil.toJsonStr(feignData);
        return JSONUtil.toBean(jsonStr, typeReference, true);
    }

    public <T> T getData(String key, TypeReference<T> typeReference) {
        Object data = this.get(key);
        String jsonStr = JSONUtil.toJsonStr(data);
        return JSONUtil.toBean(jsonStr, typeReference, true);
    }

    public R setFeignData(Object o) {
        return this.put(FeignConstant.FEIGN_RESP_KEY, o);
    }
}
