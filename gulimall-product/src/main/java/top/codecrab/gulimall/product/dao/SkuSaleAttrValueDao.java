package top.codecrab.gulimall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.codecrab.gulimall.product.entity.SkuSaleAttrValueEntity;
import top.codecrab.gulimall.product.web.vo.sku.SkuItemSaleAttrVo;

import java.util.List;

/**
 * sku销售属性&值
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {

    List<SkuItemSaleAttrVo> getSaleAttrBySpuId(@Param("spuId") Long spuId);
}
