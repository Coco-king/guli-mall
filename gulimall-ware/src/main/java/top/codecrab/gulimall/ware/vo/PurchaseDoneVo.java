package top.codecrab.gulimall.ware.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author codecrab
 * @since 2021年06月08日 17:24
 */
@NoArgsConstructor
@Data
public class PurchaseDoneVo {
    @NotNull
    private Long id;
    private List<PurchaseDoneItemsVo> items;
}
