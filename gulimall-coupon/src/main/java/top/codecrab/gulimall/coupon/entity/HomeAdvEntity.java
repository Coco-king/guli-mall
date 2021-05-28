package top.codecrab.gulimall.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * 首页轮播广告
 *
 * @author codecrab
 * @date 2021-05-28 22:26:50
 */
@Data
@TableName("sms_home_adv")
public class HomeAdvEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;

	/**
	 * 名字
	 */
	private String name;

	/**
	 * 图片地址
	 */
	private String pic;

	/**
	 * 开始时间
	 */
	private LocalDateTime startTime;

	/**
	 * 结束时间
	 */
	private LocalDateTime endTime;

	/**
	 * 状态
	 */
	private Integer status;

	/**
	 * 点击数
	 */
	private Integer clickCount;

	/**
	 * 广告详情连接地址
	 */
	private String url;

	/**
	 * 备注
	 */
	private String note;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 发布者
	 */
	private Long publisherId;

	/**
	 * 审核者
	 */
	private Long authId;

}
