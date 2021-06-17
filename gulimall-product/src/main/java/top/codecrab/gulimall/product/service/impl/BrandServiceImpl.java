package top.codecrab.gulimall.product.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.Query;
import top.codecrab.gulimall.product.dao.BrandDao;
import top.codecrab.gulimall.product.entity.BrandEntity;
import top.codecrab.gulimall.product.entity.CategoryBrandRelationEntity;
import top.codecrab.gulimall.product.service.BrandService;
import top.codecrab.gulimall.product.service.CategoryBrandRelationService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 品牌
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<BrandEntity> wrapper = new QueryWrapper<>();

        String key = MapUtil.get(params, "key", String.class);
        if (StrUtil.isNotBlank(key)) {
            wrapper.eq("brand_id", key).or().like("name", key);
        }

        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params), wrapper
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDetail(BrandEntity brand) {
        baseMapper.updateById(brand);

        categoryBrandRelationService.update(
                new UpdateWrapper<CategoryBrandRelationEntity>()
                        .set("brand_name", brand.getName())
                        .eq("brand_id", brand.getBrandId())
        );
    }

    @Override
    @Cacheable(value = "brand", key = "#root.methodName+#root.args[0]")
    public List<BrandEntity> getBrandsByIds(List<Long> brandIds) {
        return baseMapper.selectList(new QueryWrapper<BrandEntity>()
                .in("brand_id", brandIds));
    }

}
