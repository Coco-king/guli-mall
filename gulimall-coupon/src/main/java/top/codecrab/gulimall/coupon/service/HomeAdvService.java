package top.codecrab.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.gulimall.coupon.entity.HomeAdvEntity;

import java.util.Map;

/**
 * 首页轮播广告
 *
 * @author codecrab
 * @date 2021-05-28 22:26:50
 */
public interface HomeAdvService extends IService<HomeAdvEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

