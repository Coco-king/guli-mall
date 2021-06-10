package top.codecrab.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.gulimall.product.entity.ProductAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateBaseAddr(Long spuId, List<ProductAttrValueEntity> entities);

    List<ProductAttrValueEntity> getBaseAttrBySpuId(Long spuId);
}

