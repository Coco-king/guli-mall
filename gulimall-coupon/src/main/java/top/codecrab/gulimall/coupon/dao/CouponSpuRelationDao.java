package top.codecrab.gulimall.coupon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.codecrab.gulimall.coupon.entity.CouponSpuRelationEntity;

/**
 * 优惠券与产品关联
 *
 * @author codecrab
 * @date 2021-05-28 22:26:50
 */
@Mapper
public interface CouponSpuRelationDao extends BaseMapper<CouponSpuRelationEntity> {

}
