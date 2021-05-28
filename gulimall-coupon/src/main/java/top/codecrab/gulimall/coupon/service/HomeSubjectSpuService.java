package top.codecrab.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.gulimall.coupon.entity.HomeSubjectSpuEntity;

import java.util.Map;

/**
 * 专题商品
 *
 * @author codecrab
 * @date 2021-05-28 22:26:50
 */
public interface HomeSubjectSpuService extends IService<HomeSubjectSpuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

