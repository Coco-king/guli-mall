package top.codecrab.gulimall.coupon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.codecrab.gulimall.coupon.entity.SeckillSkuRelationEntity;

/**
 * 秒杀活动商品关联
 *
 * @author codecrab
 * @date 2021-05-28 22:26:50
 */
@Mapper
public interface SeckillSkuRelationDao extends BaseMapper<SeckillSkuRelationEntity> {

}
