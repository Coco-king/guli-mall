package top.codecrab.gulimall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.codecrab.gulimall.product.entity.SpuCommentEntity;

/**
 * 商品评价
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@Mapper
public interface SpuCommentDao extends BaseMapper<SpuCommentEntity> {

}
