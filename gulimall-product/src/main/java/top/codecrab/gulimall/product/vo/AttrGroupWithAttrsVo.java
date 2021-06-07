package top.codecrab.gulimall.product.vo;

import lombok.Data;
import top.codecrab.gulimall.product.entity.AttrEntity;

import java.util.List;

/**
 * @author codecrab
 * @since 2021年06月07日 18:12
 */
@Data
public class AttrGroupWithAttrsVo {
    /**
     * 分组id
     */
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

    private List<AttrEntity> attrs;
}
