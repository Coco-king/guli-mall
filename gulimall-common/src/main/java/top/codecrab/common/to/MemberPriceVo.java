package top.codecrab.common.to;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author codecrab
 * @since 2021年06月07日 20:24
 */
@NoArgsConstructor
@Data
public class MemberPriceVo {
    private Long id;
    private String name;
    private BigDecimal price;
}
