package top.codecrab.gulimall.product.web.vo.sku;

import lombok.Data;

import java.util.List;

@Data
public class SkuItemSaleAttrVo {
    private Long attrId;
    private String attrName;
    private List<AttrValueWithSkuIds> attrValues;
}
