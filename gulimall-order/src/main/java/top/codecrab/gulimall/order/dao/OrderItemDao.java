package top.codecrab.gulimall.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.codecrab.gulimall.order.entity.OrderItemEntity;

/**
 * 订单项信息
 *
 * @author codecrab
 * @date 2021-05-28 22:46:28
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {

}
