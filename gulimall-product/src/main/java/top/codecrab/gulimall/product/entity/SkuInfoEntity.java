package top.codecrab.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;

/**
 * sku信息
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@Data
@TableName("pms_sku_info")
public class SkuInfoEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * skuId
	 */
	@TableId
	private Long skuId;

	/**
	 * spuId
	 */
	private Long spuId;

	/**
	 * sku名称
	 */
	private String skuName;

	/**
	 * sku介绍描述
	 */
	private String skuDesc;

	/**
	 * 所属分类id
	 */
	private Long catalogId;

	/**
	 * 品牌id
	 */
	private Long brandId;

	/**
	 * 默认图片
	 */
	private String skuDefaultImg;

	/**
	 * 标题
	 */
	private String skuTitle;

	/**
	 * 副标题
	 */
	private String skuSubtitle;

	/**
	 * 价格
	 */
	private BigDecimal price;

	/**
	 * 销量
	 */
	private Long saleCount;

}
