package top.codecrab.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.gulimall.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author codecrab
 * @date 2021-05-28 22:46:28
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

