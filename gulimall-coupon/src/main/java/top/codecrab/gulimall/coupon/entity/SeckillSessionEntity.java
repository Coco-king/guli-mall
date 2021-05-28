package top.codecrab.gulimall.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * 秒杀活动场次
 *
 * @author codecrab
 * @date 2021-05-28 22:26:50
 */
@Data
@TableName("sms_seckill_session")
public class SeckillSessionEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;

	/**
	 * 场次名称
	 */
	private String name;

	/**
	 * 每日开始时间
	 */
	private LocalDateTime startTime;

	/**
	 * 每日结束时间
	 */
	private LocalDateTime endTime;

	/**
	 * 启用状态
	 */
	private Integer status;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

}
