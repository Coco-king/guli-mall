package top.codecrab.common.constant;

import java.util.Collections;
import java.util.Map;

/**
 * @author codecrab
 * @since 2021年06月05日 15:51
 */
public interface RedisConstant {

    String REDIS_PREFIX = "gulimall:";
    String REDIS_LOCK = REDIS_PREFIX + "lock";
    String REDIS_NULL_STR = "RESULT_IS_NULL";
    Map<String, Object> REDIS_NULL_MAP = Collections.emptyMap();

    interface CategoryConstant {
        String REDIS_KEY = REDIS_PREFIX + "catalogJson";
    }
}
