package top.codecrab.gulimall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.codecrab.gulimall.product.entity.CategoryBrandRelationEntity;

/**
 * 品牌分类关联
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@Mapper
public interface CategoryBrandRelationDao extends BaseMapper<CategoryBrandRelationEntity> {

}
