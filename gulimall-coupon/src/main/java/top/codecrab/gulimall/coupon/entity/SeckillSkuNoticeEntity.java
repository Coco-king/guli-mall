package top.codecrab.gulimall.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * 秒杀商品通知订阅
 *
 * @author codecrab
 * @date 2021-05-28 22:26:50
 */
@Data
@TableName("sms_seckill_sku_notice")
public class SeckillSkuNoticeEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;

	/**
	 * member_id
	 */
	private Long memberId;

	/**
	 * sku_id
	 */
	private Long skuId;

	/**
	 * 活动场次id
	 */
	private Long sessionId;

	/**
	 * 订阅时间
	 */
	private LocalDateTime subcribeTime;

	/**
	 * 发送时间
	 */
	private LocalDateTime sendTime;

	/**
	 * 通知方式[0-短信，1-邮件]
	 */
	private Integer noticeType;

}
