package top.codecrab.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author codecrab
 * @since 2021年06月05日 15:51
 */
public interface ProductConstant {

    String MODEL_NAME = "product";

    @Getter
    @AllArgsConstructor
    enum AttrEnum {
        ATTR_SALE(0, "销售类型"),
        ATTR_BASE(1, "基本类型");

        private final int code;
        private final String msg;
    }

    @Getter
    @AllArgsConstructor
    enum StatusEnum {
        NEW_SPU(0, "商品新建"),
        SPU_UP(1, "商品上架"),
        SPU_DOWN(2, "商品下架");

        private final int code;
        private final String msg;
    }

}
