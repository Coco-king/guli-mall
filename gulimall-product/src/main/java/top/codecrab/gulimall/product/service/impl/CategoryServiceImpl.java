package top.codecrab.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.Query;
import top.codecrab.gulimall.product.dao.CategoryDao;
import top.codecrab.gulimall.product.entity.CategoryBrandRelationEntity;
import top.codecrab.gulimall.product.entity.CategoryEntity;
import top.codecrab.gulimall.product.service.CategoryBrandRelationService;
import top.codecrab.gulimall.product.service.CategoryService;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品三级分类
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> entities = baseMapper.selectList(null);

        return entities.stream()
                .filter(item -> item.getParentCid() == 0)
                .peek(menu -> menu.setChildren(this.getChildren(menu, entities)))
                .sorted(Comparator.comparingInt(sort -> (sort.getSort() == null ? 0 : sort.getSort())))
                .collect(Collectors.toList());
    }

    @Override
    public void removeMenuByIds(List<Long> ids) {
        //TODO 删除前判断系统中是否正在使用该菜单

        //逻辑删除
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public List<Long> findCatelogPath(Long catelogId) {
        List<Long> path = this.findParentPath(catelogId, new ArrayList<>());
        Collections.reverse(path);
        return path;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDetail(CategoryEntity category) {
        baseMapper.updateById(category);

        categoryBrandRelationService.update(
                new UpdateWrapper<CategoryBrandRelationEntity>()
                        .set("catelog_name", category.getName())
                        .eq("catelog_id", category.getCatId())
        );
    }

    /**
     * 拼装指定id的父id路径 [2,25,255]
     */
    private List<Long> findParentPath(Long catelogId, List<Long> path) {
        //收集当前id
        path.add(catelogId);
        CategoryEntity categoryEntity = baseMapper.selectById(catelogId);
        if (categoryEntity.getParentCid() != 0) {
            this.findParentPath(categoryEntity.getParentCid(), path);
        }
        return path;
    }

    /**
     * 从指定列表中找到root的子列表
     *
     * @param all  所有数据列表
     * @param root 指定的节点
     */
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {
        return all.stream()
                .filter(item -> item.getParentCid().equals(root.getCatId()))
                .peek(menu -> menu.setChildren(this.getChildren(menu, all)))
                .sorted(Comparator.comparingInt(sort -> (sort.getSort() == null ? 0 : sort.getSort())))
                .collect(Collectors.toList());
    }
}
