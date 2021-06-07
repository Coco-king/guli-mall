package top.codecrab.gulimall.product.vo.spu;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author codecrab
 * @since 2021年06月07日 20:24
 */
@NoArgsConstructor
@Data
public class SkusVo {
    private List<AttrVo> attr;
    private String skuName;
    private BigDecimal price;
    private String skuTitle;
    private String skuSubtitle;
    private List<ImagesVo> images;
    private List<String> descar;
    private Integer fullCount;
    private BigDecimal discount;
    private Integer countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private Integer priceStatus;
    private List<MemberPriceVo> memberPrice;
}
