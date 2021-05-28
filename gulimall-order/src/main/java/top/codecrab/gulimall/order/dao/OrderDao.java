package top.codecrab.gulimall.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.codecrab.gulimall.order.entity.OrderEntity;

/**
 * 订单
 *
 * @author codecrab
 * @date 2021-05-28 22:46:28
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {

}
