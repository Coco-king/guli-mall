package top.codecrab.gulimall.search.vo;

import lombok.Data;
import top.codecrab.common.to.es.SkuEsModel;

import java.util.List;

/**
 * @author codecrab
 * @since 2021年06月15日 17:45
 */
@Data
public class SearchResult {

    /**
     * 所有商品集合
     */
    private List<SkuEsModel> products;

    /**
     * 总记录数
     */
    private Long totalCount;

    /**
     * 总页数
     */
    private Integer totalPage;

    /**
     * 当前页数
     */
    private Integer currPage;

    private List<Integer> pageNav;

    //================== 以上为分页参数 ===================

    /**
     * 结果涉及到的所有品牌
     */
    private List<BrandVo> brands;

    /**
     * 结果涉及到的所有分类
     */
    private List<CatalogVo> catalogs;

    /**
     * 结果涉及到的所有属性
     */
    private List<AttrVo> attrs;


    @Data
    public static class BrandVo {
        private Long brandId;

        private String brandName;

        private String brandImg;
    }

    @Data
    public static class CatalogVo {
        private Long catalogId;

        private String catalogName;
    }

    @Data
    public static class AttrVo {
        private Long attrId;

        private String attrName;

        private List<String> attrValue;
    }
}
