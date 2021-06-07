package top.codecrab.gulimall.product.vo.spu;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author codecrab
 * @since 2021年06月07日 20:24
 */
@NoArgsConstructor
@Data
public class BaseAttrsVo {
    private Long attrId;
    private String attrValues;
    private Integer showDesc;
}
