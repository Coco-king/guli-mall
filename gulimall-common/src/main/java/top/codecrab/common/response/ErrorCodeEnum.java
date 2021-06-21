package top.codecrab.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码和错误信息定义类<br>
 * 1. 错误码定义规则为5为数字<br>
 * 2. 前两位表示业务场景，最后三位表示错误码。例如：100001。10:通用 001:系统未知异常<br>
 * 3. 维护错误码后需要维护错误描述，将他们定义为枚举形式<br>
 * 错误码列表：<br>
 * &emsp;&emsp;10: 通用<br>
 * &emsp;&emsp;&emsp;&emsp;001：参数格式校验<br>
 * &emsp;&emsp;11: 商品<br>
 * &emsp;&emsp;12: 订单<br>
 * &emsp;&emsp;13: 购物车<br>
 * &emsp;&emsp;14: 物流<br>
 * &emsp;&emsp;15: 会员<br>
 *
 * @author codecrab
 * @since 2021年04月23日 8:53
 */
@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {

    /**
     * 返回给前端数据的枚举
     */
    UNKNOWN_EXCEPTION(10000, "系统未知异常"),

    /**
     * 服务器错误
     */
    BAD_SQL_GRAMMAR_ERROR(-101, "SQL语法错误"),
    SERVLET_ERROR(-102, "Servlet请求异常"),
    UPLOAD_ERROR(-103, "文件上传错误"),

    /**
     * 参数校验
     */
    VALID_EXCEPTION(10001, "参数格式校验失败"),
    PHONE_IS_NOT_LEGAL(10003, "手机号码格式不合法"),
    VALID_NOT_ALL_NULL_EXCEPTION(10102, "更新的参数不能全部为空"),
    CATEGORY_NAME_NULL_ERROR(10203, "商品分类名称不能为空"),
    REMOVE_FILE_URL_NULL_ERROR(10304, "删除的图片路径不能为空"),
    PHONE_IS_EXIST(15100, "手机号码已被注册"),
    USERNAME_IS_EXIST(15101, "用户名已被注册"),
    USERNAME_OR_PASSWORD_ERROR(15102, "用户名或密码错误"),

    PRODUCT_UP_ERROR(11000, "商品上架异常"),

    /**
     * 业务逻辑错误
     */
    PURCHASE_STATUS_NOT_CREATED_OR_ASSIGNED(14101, "采购单状态必须是新建或已分配才可以添加采购需求"),
    PURCHASE_DETAIL_STATUS_NOT_CREATED_OR_ASSIGNED(14102, "采购项状态必须是新建或已分配才可以合并"),
    PURCHASE_DETAIL_STATUS_MUST_BE_BUYING(14103, "采购项状态必须是采购中才可以标记完成"),
    PURCHASE_DETAIL_STATUS_UNKNOWN(14104, "采购项状态有误（采购成功/采购失败）"),
    PURCHASE_DETAIL_NOT_FIND(14201, "采购项未找到"),

    /**
     * 业务限流
     */
    ALIYUN_SMS_LIMIT_CONTROL_ERROR(10002, "短信发送过于频繁"),
    ;

    private final Integer code;

    private final String msg;
}
