package top.codecrab.gulimall.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * 秒杀活动
 *
 * @author codecrab
 * @date 2021-05-28 22:26:50
 */
@Data
@TableName("sms_seckill_promotion")
public class SeckillPromotionEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;

	/**
	 * 活动标题
	 */
	private String title;

	/**
	 * 开始日期
	 */
	private LocalDateTime startTime;

	/**
	 * 结束日期
	 */
	private LocalDateTime endTime;

	/**
	 * 上下线状态
	 */
	private Integer status;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 创建人
	 */
	private Long userId;

}
