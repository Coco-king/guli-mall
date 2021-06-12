package top.codecrab.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void removeMenuByIds(List<Long> ids);

    /**
     * 根据分类id找到他上级所有id
     *
     * @param catelogId 分类id
     */
    List<Long> findCatelogPath(Long catelogId);


    /**
     * 更新分类和关联的冗余字段
     */
    void updateDetail(CategoryEntity category);

    List<CategoryEntity> getLevel1Categories();

    /**
     * @return "Map<String, List<Catalog2Vo>>"类型数据
     */
    Object getCatalogJson();
}

