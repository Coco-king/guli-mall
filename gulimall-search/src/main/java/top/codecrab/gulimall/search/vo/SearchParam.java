package top.codecrab.gulimall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * @author codecrab
 * @since 2021年06月15日 17:25
 */
@Data
public class SearchParam {

    /**
     * 检索关键字
     */
    private String keyword;

    /**
     * 品牌id
     */
    private List<Long> brandId;

    /**
     * 分类id
     */
    private Long catalogId;

    /**
     * 排序： sort=price/saleCount/hotScore_desc/asc
     */
    private String sort;

    /**
     * 价格区间：price=0_400 或者 price=_200 或者 price=200_
     */
    private String price;

    /**
     * 是否有库存： stock=0/1；1只显示有货，0或者不传都会显示
     */
    private Integer hasStock;

    /**
     * 可以传入多个； attrs=1_3G:4G；1号属性值为3G或者4G
     */
    private List<String> attrs;

    /**
     * 当前页数
     */
    private Integer currPage = 1;

    /**
     * 每页记录数
     */
    private Integer pageSize;

}
