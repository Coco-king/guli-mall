package top.codecrab.common.to;

import lombok.Data;

/**
 * @author codecrab
 * @since 2021年06月10日 18:10
 */
@Data
public class SkuHasStockTo {
    private Long skuId;
    private Boolean hasStock;
}
