package top.codecrab.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author codecrab
 * @since 2021年06月05日 15:51
 */
public class ProductConstant {

    @Getter
    @AllArgsConstructor
    public enum AttrEnum {
        ATTR_SALE(0, "销售类型"),
        ATTR_BASE(1, "基本类型");

        private final int code;
        private final String msg;
    }

}
