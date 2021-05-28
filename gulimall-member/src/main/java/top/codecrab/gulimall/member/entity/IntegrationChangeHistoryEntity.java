package top.codecrab.gulimall.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * 积分变化历史记录
 *
 * @author codecrab
 * @date 2021-05-28 22:40:42
 */
@Data
@TableName("ums_integration_change_history")
public class IntegrationChangeHistoryEntity implements Serializable {

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
	 * create_time
	 */
	private LocalDateTime createTime;

	/**
	 * 变化的值
	 */
	private Integer changeCount;

	/**
	 * 备注
	 */
	private String note;

	/**
	 * 来源[0->购物；1->管理员修改;2->活动]
	 */
	private Integer sourceTyoe;

}
