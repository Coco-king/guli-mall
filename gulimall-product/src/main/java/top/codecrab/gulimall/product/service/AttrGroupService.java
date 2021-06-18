package top.codecrab.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.gulimall.product.entity.AttrGroupEntity;
import top.codecrab.gulimall.product.vo.AttrGroupWithAttrsVo;
import top.codecrab.gulimall.product.web.vo.sku.SkuItemAttrGroupVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long categoryId);

    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrByCatelogId(Long catelogId);

    List<SkuItemAttrGroupVo> getAttrGroupWithAttrBySpuId(Long spuId, Long catalogId);
}

