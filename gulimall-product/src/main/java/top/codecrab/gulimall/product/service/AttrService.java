package top.codecrab.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.gulimall.product.entity.AttrEntity;
import top.codecrab.gulimall.product.vo.AttrRelationVo;
import top.codecrab.gulimall.product.vo.AttrResponseVo;
import top.codecrab.gulimall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author codecrab
 * @date 2021-05-28 22:19:47
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseList(Map<String, Object> params, Long catelogId, String attrType);

    void updateAttr(AttrVo attr);

    AttrResponseVo getAttrResponseVo(Long attrId);

    List<AttrEntity> findAttrRelation(Long attrGroupId);

    void removeRelations(List<AttrRelationVo> relationVos);
}

