package top.codecrab.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.Query;
import top.codecrab.gulimall.product.dao.SkuImagesDao;
import top.codecrab.gulimall.product.entity.SkuImagesEntity;
import top.codecrab.gulimall.product.service.SkuImagesService;

import java.util.List;
import java.util.Map;

/**
 * sku图片
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@Service("skuImagesService")
public class SkuImagesServiceImpl extends ServiceImpl<SkuImagesDao, SkuImagesEntity> implements SkuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuImagesEntity> page = this.page(
                new Query<SkuImagesEntity>().getPage(params),
                new QueryWrapper<SkuImagesEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuImagesEntity> getSkuImagesBySkuId(Long skuId) {
        return baseMapper.selectList(new QueryWrapper<SkuImagesEntity>()
                .eq("sku_id", skuId));
    }

}
