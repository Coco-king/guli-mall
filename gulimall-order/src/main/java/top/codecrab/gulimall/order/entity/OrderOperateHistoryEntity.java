package top.codecrab.gulimall.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * 订单操作历史记录
 *
 * @author codecrab
 * @date 2021-05-28 22:46:28
 */
@Data
@TableName("oms_order_operate_history")
public class OrderOperateHistoryEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;

	/**
	 * 订单id
	 */
	private Long orderId;

	/**
	 * 操作人[用户；系统；后台管理员]
	 */
	private String operateMan;

	/**
	 * 操作时间
	 */
	private LocalDateTime createTime;

	/**
	 * 订单状态【0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单】
	 */
	private Integer orderStatus;

	/**
	 * 备注
	 */
	private String note;

}