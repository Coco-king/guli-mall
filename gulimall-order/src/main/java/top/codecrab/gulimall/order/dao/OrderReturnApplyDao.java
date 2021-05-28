package top.codecrab.gulimall.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.codecrab.gulimall.order.entity.OrderReturnApplyEntity;

/**
 * 订单退货申请
 *
 * @author codecrab
 * @date 2021-05-28 22:46:28
 */
@Mapper
public interface OrderReturnApplyDao extends BaseMapper<OrderReturnApplyEntity> {

}
