package top.codecrab.gulimall.product.web.vo.sku;

import lombok.Data;

import java.util.List;

@Data
public class SkuItemAttrGroupVo {
    private String groupName;
    private List<SkuItemBaseAttrVo> baseAttrVos;
}
