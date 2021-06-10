package top.codecrab.gulimall.ware.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.codecrab.common.response.R;
import top.codecrab.common.to.SkuHasStockTo;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.Query;
import top.codecrab.gulimall.ware.client.ProductFeignClient;
import top.codecrab.gulimall.ware.dao.WareSkuDao;
import top.codecrab.gulimall.ware.entity.WareSkuEntity;
import top.codecrab.gulimall.ware.service.WareSkuService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品库存
 *
 * @author codecrab
 * @date 2021-05-28 22:50:44
 */
@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Resource
    private ProductFeignClient productFeignClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String skuId = MapUtil.getStr(params, "skuId");
        String wareId = MapUtil.getStr(params, "wareId");

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                new QueryWrapper<WareSkuEntity>()
                        .eq(StrUtil.isNotBlank(skuId), "sku_id", skuId)
                        .eq(StrUtil.isNotBlank(wareId), "ware_id", wareId)
        );

        return new PageUtils(page);
    }

    @Override
    public void updateStock(Long skuId, Long wareId, Integer skuNum) {
        List<WareSkuEntity> entities = baseMapper.selectList(new QueryWrapper<WareSkuEntity>()
                .eq("sku_id", skuId)
                .eq("ware_id", wareId));
        if (CollectionUtil.isEmpty(entities)) {
            String skuName = "";

            try {
                R info = productFeignClient.skuInfoName(skuId);
                if (info != null) {
                    skuName = (String) info.getFeignData();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setSkuName(skuName);
            baseMapper.insert(wareSkuEntity);
        } else {
            baseMapper.updateStock(skuId, wareId, skuNum);
        }
    }

    @Override
    public List<SkuHasStockTo> getSkusHasStock(List<Long> skuIds) {
        return skuIds.stream()
                .map(skuId -> {
                    Long count = baseMapper.getSkuHasStock(skuId);
                    SkuHasStockTo to = new SkuHasStockTo();
                    to.setSkuId(skuId);
                    to.setHasStock((count != null && count > 0));
                    return to;
                })
                .collect(Collectors.toList());
    }

}
