package top.codecrab.gulimall.product.vo.spu;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author codecrab
 * @since 2021年06月07日 20:19
 */
@NoArgsConstructor
@Data
public class SpuSaveVo {
    private String spuName;
    private String spuDescription;
    private Long catalogId;
    private Long brandId;
    private BigDecimal weight;
    private Integer publishStatus;
    private List<String> decript;
    private List<String> images;
    private BoundsVo bounds;
    private List<BaseAttrsVo> baseAttrs;
    private List<SkusVo> skus;
}
