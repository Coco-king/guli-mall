package top.codecrab.gulimall.product.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author codecrab
 * @since 2021年06月05日 14:14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AttrResponseVo extends AttrVo {
    /**
     * 所属分类名字
     */
    private String catelogName;

    /**
     * 所属分组名字
     */
    private String groupName;

    /**
     * 全路径 [父ID, 子ID, 孙ID]
     */
    private List<Long> catelogPath;
}
