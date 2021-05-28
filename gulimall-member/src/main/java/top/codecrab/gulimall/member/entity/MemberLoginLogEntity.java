package top.codecrab.gulimall.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * 会员登录记录
 *
 * @author codecrab
 * @date 2021-05-28 22:40:42
 */
@Data
@TableName("ums_member_login_log")
public class MemberLoginLogEntity implements Serializable {

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
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * ip
	 */
	private String ip;

	/**
	 * city
	 */
	private String city;

	/**
	 * 登录类型[1-web，2-app]
	 */
	private Integer loginType;

}
