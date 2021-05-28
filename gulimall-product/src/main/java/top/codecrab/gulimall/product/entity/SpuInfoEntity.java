package top.codecrab.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * spu信息
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@Data
@TableName("pms_spu_info")
public class SpuInfoEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 商品id
	 */
	@TableId
	private Long id;

	/**
	 * 商品名称
	 */
	private String spuName;

	/**
	 * 商品描述
	 */
	private String spuDescription;

	/**
	 * 所属分类id
	 */
	private Long catalogId;

	/**
	 * 品牌id
	 */
	private Long brandId;

	/**
	 * 
	 */
	private BigDecimal weight;

	/**
	 * 上架状态[0 - 下架，1 - 上架]
	 */
	private Integer publishStatus;

	/**
	 * 
	 */
	private LocalDateTime createTime;

	/**
	 * 
	 */
	private LocalDateTime updateTime;

}
