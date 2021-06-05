package top.codecrab.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 属性分组
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@Data
@TableName("pms_attr_group")
public class AttrGroupEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 分组id
	 */
	@TableId
	private Long attrGroupId;

	/**
	 * 组名
	 */
	private String attrGroupName;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 描述
	 */
	private String descript;

	/**
	 * 组图标
	 */
	private String icon;

	/**
	 * 所属分类id
	 */
	private Long catelogId;

    /**
     * 全路径 [父ID, 子ID, 孙ID]
     */
    @TableField(exist = false)
	private List<Long> catelogPath;

}
