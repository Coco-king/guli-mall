package top.codecrab.gulimall.ware.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * 采购信息
 *
 * @author codecrab
 * @date 2021-05-28 22:50:44
 */
@Data
@TableName("wms_purchase")
public class PurchaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;

	/**
	 * 
	 */
	private Long assigneeId;

	/**
	 * 
	 */
	private String assigneeName;

	/**
	 * 
	 */
	private String phone;

	/**
	 * 
	 */
	private Integer priority;

	/**
	 * 
	 */
	private Integer status;

	/**
	 * 
	 */
	private Long wareId;

	/**
	 * 
	 */
	private BigDecimal amount;

	/**
	 * 
	 */
	private LocalDateTime createTime;

	/**
	 * 
	 */
	private LocalDateTime updateTime;

}
