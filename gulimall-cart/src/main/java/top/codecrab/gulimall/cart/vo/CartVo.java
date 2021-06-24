package top.codecrab.gulimall.cart.vo;

import cn.hutool.core.collection.CollectionUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author codecrab
 * @since 2021年06月24日 16:14
 */
public class CartVo {
    private List<CartItemVo> items;

    /**
     * 商品的数量
     */
    private Integer totalNum;

    /**
     * 商品的类型
     */
    private Integer totalType;

    private BigDecimal totalAmount;

    private BigDecimal reduce = BigDecimal.ZERO;

    public List<CartItemVo> getItems() {
        return items;
    }

    public void setItems(List<CartItemVo> items) {
        this.items = items;
    }

    /**
     * 获得商品数量
     */
    public Integer getTotalNum() {
        int count = 0;
        if (CollectionUtil.isNotEmpty(items)) {
            for (CartItemVo item : items) {
                count += item.getCount();
            }
        }
        return count;
    }

    public Integer getTotalType() {
        if (CollectionUtil.isNotEmpty(items)) {
            return items.size();
        }
        return 0;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal totalAmount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(items)) {
            for (CartItemVo item : items) {
                totalAmount = totalAmount.add(item.getTotalPrice());
            }
        }
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getReduce() {
        return reduce;
    }

    public void setReduce(BigDecimal reduce) {
        this.reduce = reduce;
    }
}
