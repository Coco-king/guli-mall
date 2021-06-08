package top.codecrab.gulimall.ware.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author codecrab
 * @since 2021年06月08日 17:26
 */
@NoArgsConstructor
@Data
public class PurchaseDoneItemsVo {
    private Long itemId;
    private Integer status;
    private String reason;
}
