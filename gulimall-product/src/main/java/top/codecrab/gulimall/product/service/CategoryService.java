package top.codecrab.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.gulimall.product.entity.CategoryEntity;

import java.util.Map;

/**
 * 商品三级分类
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

