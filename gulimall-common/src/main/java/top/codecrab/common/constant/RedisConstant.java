package top.codecrab.common.constant;

import java.util.Collections;
import java.util.Map;

/**
 * @author codecrab
 * @since 2021年06月05日 15:51
 */
public interface RedisConstant {

    String PREFIX = "gulimall:";
    String LOCK = PREFIX + "lock";
    String NULL_STR = "RESULT_IS_NULL";
    Map<String, Object> NULL_MAP = Collections.emptyMap();

    static String REDISSON_LOCK(String model, String type) {
        return PREFIX + model + ":" + type + "-lock";
    }

    interface Category {
        String TYPE = "catalogJson";
        String KEY = PREFIX + TYPE;
    }

    interface Auth {
        String CODE = PREFIX + "sms:code:";
    }
}
