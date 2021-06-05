package top.codecrab.common.utils;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import top.codecrab.common.exception.RRException;
import top.codecrab.common.response.ErrorCodeEnum;

/**
 * @author codecrab
 */
@Slf4j
public abstract class Assert {

    /**
     * 断言对象不为空
     * 如果对象obj为空，则抛出异常
     *
     * @param obj 待判断对象
     */
    public static void notNull(Object obj, ErrorCodeEnum errorCodeEnum) {
        if (ObjectUtil.isNull(obj)) {
            log.info("obj is null...............");
            throw new RRException(errorCodeEnum);
        }
    }

    /**
     * 断言对象为空
     * 如果对象obj不为空，则抛出异常
     */
    public static void isNull(Object object, ErrorCodeEnum errorCodeEnum) {
        if (ObjectUtil.isNotNull(object)) {
            log.info("obj is not null......");
            throw new RRException(errorCodeEnum);
        }
    }

    /**
     * 断言表达式为真
     * 如果不为真，则抛出异常
     *
     * @param expression 是否成功
     */
    public static void isTrue(Boolean expression, ErrorCodeEnum errorCodeEnum) {
        if (BooleanUtil.isFalse(expression)) {
            log.info("fail...............");
            throw new RRException(errorCodeEnum);
        }
    }

    /**
     * 断言表达式为假
     * 如果不为假，则抛出异常
     *
     * @param expression 是否成功
     */
    public static void isFalse(Boolean expression, ErrorCodeEnum errorCodeEnum) {
        if (BooleanUtil.isTrue(expression)) {
            log.info("true...............");
            throw new RRException(errorCodeEnum);
        }
    }

    /**
     * 断言两个对象不相等
     * 如果相等，则抛出异常
     */
    public static void notEquals(Object m1, Object m2, ErrorCodeEnum errorCodeEnum) {
        if (ObjectUtil.equals(m1, m2)) {
            log.info("equals...............");
            throw new RRException(errorCodeEnum);
        }
    }

    /**
     * 断言两个对象相等
     * 如果不相等，则抛出异常
     */
    public static void equals(Object m1, Object m2, ErrorCodeEnum errorCodeEnum) {
        if (ObjectUtil.notEqual(m1, m2)) {
            log.info("not equals...............");
            throw new RRException(errorCodeEnum);
        }
    }

    /**
     * 断言参数不为空
     * 如果为空，则抛出异常
     */
    public static void notEmpty(String s, ErrorCodeEnum errorCodeEnum) {
        if (StrUtil.isEmpty(s)) {
            log.info("is empty...............");
            throw new RRException(errorCodeEnum);
        }
    }

    /**
     * 断言参数不为空白字符
     * 如果为空白字符，则抛出异常
     */
    public static void notBlank(String s, ErrorCodeEnum errorCodeEnum) {
        if (StrUtil.isBlank(s)) {
            log.info("is blank...............");
            throw new RRException(errorCodeEnum);
        }
    }

    public static void isNotAllEmpty(ErrorCodeEnum errorCodeEnum, Object... objects) {
        if (ObjectUtil.isAllEmpty(objects)) {
            throw new RRException(errorCodeEnum);
        }
    }
}
