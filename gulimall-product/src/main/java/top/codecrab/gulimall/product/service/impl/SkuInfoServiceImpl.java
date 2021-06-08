package top.codecrab.gulimall.product.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.Query;
import top.codecrab.gulimall.product.dao.SkuInfoDao;
import top.codecrab.gulimall.product.entity.SkuInfoEntity;
import top.codecrab.gulimall.product.service.SkuInfoService;

import java.math.BigDecimal;
import java.util.Map;

/**
 * sku信息
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = MapUtil.getStr(params, "key");
        String brandId = MapUtil.getStr(params, "brandId");
        String catelogId = MapUtil.getStr(params, "catelogId");
        BigDecimal min = null;
        BigDecimal max = null;
        try {
            min = MapUtil.get(params, "min", BigDecimal.class);
            max = MapUtil.get(params, "max", BigDecimal.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
                        .eq(StrUtil.isNotBlank(brandId) && !"0".equals(brandId), "brand_id", brandId)
                        .eq(StrUtil.isNotBlank(catelogId) && !"0".equals(catelogId), "catalog_id", catelogId)
                        .ge(min != null && min.compareTo(BigDecimal.ZERO) > 0, "price", min)
                        .le(max != null && max.compareTo(BigDecimal.ZERO) > 0, "price", max)
                        .and(StrUtil.isNotBlank(key), wrapper -> wrapper
                                .eq("sku_id", key).or()
                                .like("sku_name", key)
                        )
        );

        return new PageUtils(page);
    }

}
