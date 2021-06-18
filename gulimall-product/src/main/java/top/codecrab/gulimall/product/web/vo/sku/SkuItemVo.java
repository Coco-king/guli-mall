package top.codecrab.gulimall.product.web.vo.sku;

import lombok.Data;
import top.codecrab.gulimall.product.entity.SkuImagesEntity;
import top.codecrab.gulimall.product.entity.SkuInfoEntity;
import top.codecrab.gulimall.product.entity.SpuInfoDescEntity;

import java.util.List;

/**
 * @author codecrab
 * @since 2021年06月18日 16:17
 */
@Data
public class SkuItemVo {
    private SkuInfoEntity skuInfo;
    private List<SkuImagesEntity> skuImages;
    private SpuInfoDescEntity desc;
    private Boolean hasStock = true;
    private String brandName = "";
    private List<SkuItemSaleAttrVo> saleAttrVos;
    private List<SkuItemAttrGroupVo> groupVos;
    private List<SkuItemAttrGroupVo> showDesc;
}
