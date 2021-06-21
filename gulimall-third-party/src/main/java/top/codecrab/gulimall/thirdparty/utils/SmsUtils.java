package top.codecrab.gulimall.thirdparty.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import top.codecrab.common.constant.ThirdPartyConstant;
import top.codecrab.gulimall.thirdparty.config.SmsProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author codecrab
 * @since 2021年06月21日 10:44
 */
public class SmsUtils {

    /**
     * templateCode是Access模式、templateId是AppCode模式
     *
     * @param param {"code": "111111","phone": "phone","minute": 5,"templateCode": "xxx","templateId": "xxx"}
     */
    public static void sendSms(Map<String, Object> param) throws Exception {
        String phone = MapUtil.getStr(param, "phone");
        Integer minute = MapUtil.getInt(param, "minute");
        Boolean useAccess = MapUtil.getBool(param, "useAccess");

        if (minute == null || minute <= 0) {
            minute = ThirdPartyConstant.SMS.EXPIRE_DATE;
            param.put("minute", minute);
        }

        if (useAccess == null || useAccess) {
            String templateCode = MapUtil.getStr(param, "templateCode");
            if (StrUtil.isBlank(templateCode)) {
                templateCode = SmsProperties.TEMPLATE_CODE;
            }
            sendSmsByAccess(phone, templateCode, param);
        } else {
            String templateId = MapUtil.getStr(param, "templateId");
            if (StrUtil.isBlank(templateId)) {
                templateId = SmsProperties.TEMPLATE_ID;
            }
            sendSmsByAppCode(phone, templateId, param);
        }
    }

    public static SendSmsResponse sendSmsByAccess(String phone, String templateCode, Map<String, Object> param) throws Exception {
        // 使用AK&SK初始化账号Client
        Config config = new Config()
                .setAccessKeyId(SmsProperties.KEY_ID)
                .setAccessKeySecret(SmsProperties.KEY_SECRET);
        // 访问的域名
        config.setEndpoint(SmsProperties.END_POINT);
        Client client = new Client(config);
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setTemplateParam(new Gson().toJson(param))
                .setTemplateCode(templateCode)
                .setSignName(SmsProperties.SIGN_NAME)
                .setPhoneNumbers(phone);
        return client.sendSms(sendSmsRequest);
    }

    public static HttpResponse sendSmsByAppCode(String phone, String templateId, Map<String, Object> param) throws Exception {
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + SmsProperties.APPCODE);
        Map<String, String> queries = new HashMap<>();
        queries.put("mobile", phone);
        //**code**:12345,**minute**:5
        queries.put("param", JSONUtil.toJsonStr(param));
        queries.put("smsSignId", SmsProperties.SIGN_ID);
        queries.put("templateId", templateId);
        Map<String, String> bodies = new HashMap<>();
        //获取response的body
        //System.out.println(EntityUtils.toString(response.getEntity()));
        return HttpUtils.doPost(
                SmsProperties.HOST,
                SmsProperties.PATH,
                SmsProperties.METHOD,
                headers,
                queries,
                bodies
        );
    }
}
