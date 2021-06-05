package top.codecrab.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import top.codecrab.common.valid.AddGroup;
import top.codecrab.common.valid.ListValue;
import top.codecrab.common.valid.UpdateGroup;
import top.codecrab.common.valid.UpdateStatusGroup;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * 品牌
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    @TableId
    @Null(groups = AddGroup.class, message = "新增品牌的ID必须为空")
    @NotNull(groups = UpdateGroup.class, message = "更新品牌ID不能为空")
    private Long brandId;

    /**
     * 品牌名
     */
    @NotBlank(groups = {AddGroup.class, UpdateGroup.class}, message = "品牌名不能为空")
    private String name;

    /**
     * 品牌logo地址
     */
    @NotBlank(groups = AddGroup.class, message = "品牌LogoURL地址不能为空")
    @URL(groups = {AddGroup.class, UpdateGroup.class}, message = "品牌Logo不是一个有效的URL地址")
    private String logo;

    /**
     * 介绍
     */
    private String descript;

    /**
     * 显示状态[0-不显示；1-显示]
     */
    @NotNull(groups = {AddGroup.class, UpdateStatusGroup.class}, message = "显示状态不能为空")
    @ListValue(groups = {AddGroup.class, UpdateStatusGroup.class}, value = {0, 1}, message = "显示状态只包含0或1")
    private Integer showStatus;

    /**
     * 检索首字母
     */
    @NotBlank(groups = AddGroup.class, message = "检索首字母不能为空")
    @Length(groups = {AddGroup.class, UpdateGroup.class}, min = 1, max = 1, message = "检索首字母长度必须为1")
    @Pattern(groups = {AddGroup.class, UpdateGroup.class}, regexp = "[a-zA-Z]", message = "检索首字母必须在a-z或A-Z之间")
    private String firstLetter;

    /**
     * 排序
     */
    @NotNull(groups = AddGroup.class, message = "排序不能为空")
    @Min(groups = {AddGroup.class, UpdateGroup.class}, value = 0, message = "排序最小为0")
    private Integer sort;

}
