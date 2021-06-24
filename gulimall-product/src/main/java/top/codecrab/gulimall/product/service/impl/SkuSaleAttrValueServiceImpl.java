package top.codecrab.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.Query;
import top.codecrab.gulimall.product.dao.SkuSaleAttrValueDao;
import top.codecrab.gulimall.product.entity.SkuSaleAttrValueEntity;
import top.codecrab.gulimall.product.service.SkuSaleAttrValueService;
import top.codecrab.gulimall.product.web.vo.sku.SkuItemSaleAttrVo;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuItemSaleAttrVo> getSaleAttrBySpuId(Long spuId) {
        return baseMapper.getSaleAttrBySpuId(spuId);
    }

    @Override
    public List<String> getListBySkuId(Long skuId) {
        return baseMapper.getListBySkuId(skuId);
    }

}
