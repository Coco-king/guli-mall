package top.codecrab.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author codecrab
 * @since 2021年06月08日 15:43
 */
@Data
public class MergeVo {
    private List<Long> items;
    private Long purchaseId;
}
