package top.codecrab.gulimall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.codecrab.gulimall.product.entity.AttrGroupEntity;
import top.codecrab.gulimall.product.web.vo.sku.SkuItemAttrGroupVo;

import java.util.List;

/**
 * 属性分组
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {

    List<SkuItemAttrGroupVo> getAttrGroupWithAttrBySkuId(@Param("spuId") Long spuId, @Param("catalogId") Long catalogId);
}
