package top.codecrab.gulimall.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * 会员收藏的商品
 *
 * @author codecrab
 * @date 2021-05-28 22:40:42
 */
@Data
@TableName("ums_member_collect_spu")
public class MemberCollectSpuEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;

	/**
	 * 会员id
	 */
	private Long memberId;

	/**
	 * spu_id
	 */
	private Long spuId;

	/**
	 * spu_name
	 */
	private String spuName;

	/**
	 * spu_img
	 */
	private String spuImg;

	/**
	 * create_time
	 */
	private LocalDateTime createTime;

}
