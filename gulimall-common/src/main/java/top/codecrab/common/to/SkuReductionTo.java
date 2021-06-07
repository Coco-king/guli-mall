package top.codecrab.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author codecrab
 * @since 2021年06月07日 22:38
 */
@Data
public class SkuReductionTo {
    private Long skuId;
    private Integer fullCount;
    private BigDecimal discount;
    private Integer countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private Integer priceStatus;
    private List<MemberPriceVo> memberPrice;
}
