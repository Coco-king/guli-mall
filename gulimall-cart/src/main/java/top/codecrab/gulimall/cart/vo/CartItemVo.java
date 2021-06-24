package top.codecrab.gulimall.cart.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author codecrab
 * @since 2021年06月24日 16:09
 */
public class CartItemVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long skuId;
    private Boolean check = true;
    private String title;
    private String defaultImg;
    private List<String> skuAttrs;
    private BigDecimal price;
    private Integer count;
    private BigDecimal totalPrice;

    public BigDecimal getTotalPrice() {
        return price.multiply(new BigDecimal(count));
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDefaultImg() {
        return defaultImg;
    }

    public void setDefaultImg(String defaultImg) {
        this.defaultImg = defaultImg;
    }

    public List<String> getSkuAttrs() {
        return skuAttrs;
    }

    public void setSkuAttrs(List<String> skuAttrs) {
        this.skuAttrs = skuAttrs;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
    }
}
