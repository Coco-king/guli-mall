package top.codecrab.gulimall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.Query;
import top.codecrab.gulimall.order.dao.OrderReturnApplyDao;
import top.codecrab.gulimall.order.entity.OrderReturnApplyEntity;
import top.codecrab.gulimall.order.service.OrderReturnApplyService;

import java.util.Map;

/**
 * 订单退货申请
 *
 * @author codecrab
 * @date 2021-05-28 22:46:28
 */
@Service("orderReturnApplyService")
public class OrderReturnApplyServiceImpl extends ServiceImpl<OrderReturnApplyDao, OrderReturnApplyEntity> implements OrderReturnApplyService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderReturnApplyEntity> page = this.page(
                new Query<OrderReturnApplyEntity>().getPage(params),
                new QueryWrapper<OrderReturnApplyEntity>()
        );

        return new PageUtils(page);
    }

}
