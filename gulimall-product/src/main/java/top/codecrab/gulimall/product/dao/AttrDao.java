package top.codecrab.gulimall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.codecrab.gulimall.product.entity.AttrEntity;

import java.util.Set;

/**
 * 商品属性
 *
 * @author codecrab
 * @date 2021-05-28 22:19:47
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {

    Set<Long> selectSearchAttrIds(@Param("attrIds") Set<Long> attrIds);
}
