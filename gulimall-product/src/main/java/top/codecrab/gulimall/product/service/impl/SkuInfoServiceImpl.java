package top.codecrab.gulimall.product.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.codecrab.common.response.R;
import top.codecrab.common.to.SkuHasStockTo;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.Query;
import top.codecrab.gulimall.product.client.WareFeignClient;
import top.codecrab.gulimall.product.dao.SkuInfoDao;
import top.codecrab.gulimall.product.entity.BrandEntity;
import top.codecrab.gulimall.product.entity.SkuImagesEntity;
import top.codecrab.gulimall.product.entity.SkuInfoEntity;
import top.codecrab.gulimall.product.entity.SpuInfoDescEntity;
import top.codecrab.gulimall.product.service.*;
import top.codecrab.gulimall.product.web.vo.sku.SkuItemAttrGroupVo;
import top.codecrab.gulimall.product.web.vo.sku.SkuItemBaseAttrVo;
import top.codecrab.gulimall.product.web.vo.sku.SkuItemSaleAttrVo;
import top.codecrab.gulimall.product.web.vo.sku.SkuItemVo;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * sku信息
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@Slf4j
@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Resource
    private AttrGroupService attrGroupService;

    @Resource
    private SkuImagesService skuImagesService;

    @Resource
    private SpuInfoDescService spuInfoDescService;

    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Resource
    private BrandService brandService;

    @Resource
    private WareFeignClient wareFeignClient;

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

    @Override
    public SkuItemVo item(Long skuId) {
        //查询Sku的信息
        SkuInfoEntity skuInfoEntity = baseMapper.selectById(skuId);
        Long spuId = skuInfoEntity.getSpuId();
        Long catalogId = skuInfoEntity.getCatalogId();
        //查询Sku对应的图片集合
        List<SkuImagesEntity> skuImagesEntities = skuImagesService.getSkuImagesBySkuId(skuInfoEntity.getSkuId());
        //查询指定的Spu介绍
        SpuInfoDescEntity descEntity = spuInfoDescService.getById(spuId);
        //查询Spu对应的基本属性信息
        List<SkuItemAttrGroupVo> itemAttrGroupVos = attrGroupService.getAttrGroupWithAttrBySpuId(spuId, catalogId);
        //查询Spu包含的所有销售属性
        List<SkuItemSaleAttrVo> saleAttrVos = skuSaleAttrValueService.getSaleAttrBySpuId(spuId);
        //查询品牌名
        BrandEntity brandEntity = brandService.getById(skuInfoEntity.getBrandId());

        SkuItemVo skuItemVo = new SkuItemVo();
        //远程查询库存
        try {
            R r = wareFeignClient.hasStock(Collections.singletonList(skuId));
            if (r.getCode() == 0) {
                List<SkuHasStockTo> stock = r.getFeignData(new TypeReference<List<SkuHasStockTo>>() {
                });
                if (CollectionUtil.isNotEmpty(stock)) {
                    skuItemVo.setHasStock(stock.get(0).getHasStock());
                }
            }
        } catch (Exception e) {
            log.error("远程调用 hasStock 失败：skuId：" + skuId, e);
        }
        //组装需要快速展示的数据
        List<SkuItemAttrGroupVo> groupVos = new ArrayList<>();
        for (SkuItemAttrGroupVo vo : itemAttrGroupVos) {
            SkuItemAttrGroupVo groupVo = new SkuItemAttrGroupVo();
            List<SkuItemBaseAttrVo> collect = vo.getBaseAttrVos().stream()
                    .filter(attr -> attr.getShowDesc() != null && attr.getShowDesc() == 1)
                    .collect(Collectors.toList());
            groupVo.setGroupName("");
            groupVo.setBaseAttrVos(collect);
            groupVos.add(groupVo);
        }

        skuItemVo.setShowDesc(groupVos);
        skuItemVo.setSkuInfo(skuInfoEntity);
        skuItemVo.setSkuImages(skuImagesEntities);
        skuItemVo.setDesc(descEntity);
        skuItemVo.setSaleAttrVos(saleAttrVos);
        skuItemVo.setGroupVos(itemAttrGroupVos);
        skuItemVo.setBrandName(brandEntity.getName());
        return skuItemVo;
    }
}
