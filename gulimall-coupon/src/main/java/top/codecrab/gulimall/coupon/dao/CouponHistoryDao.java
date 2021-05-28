package top.codecrab.gulimall.coupon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.codecrab.gulimall.coupon.entity.CouponHistoryEntity;

/**
 * 优惠券领取历史记录
 *
 * @author codecrab
 * @date 2021-05-28 22:26:50
 */
@Mapper
public interface CouponHistoryDao extends BaseMapper<CouponHistoryEntity> {

}
