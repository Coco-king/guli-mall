package top.codecrab.gulimall.product.vo.spu;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author codecrab
 * @since 2021年06月07日 20:24
 */
@NoArgsConstructor
@Data
public class BoundsVo {
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
