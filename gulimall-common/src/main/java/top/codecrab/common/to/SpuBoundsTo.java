package top.codecrab.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author codecrab
 * @since 2021年06月07日 22:31
 */
@Data
public class SpuBoundsTo {
    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
