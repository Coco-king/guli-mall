package top.codecrab.gulimall.product.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
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
    private ThreadPoolExecutor executor;

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
    @SneakyThrows
    public SkuItemVo item(Long skuId) {
        //long l = System.currentTimeMillis();
        SkuItemVo skuItemVo = new SkuItemVo();

        //查询Sku的信息
        CompletableFuture<SkuInfoEntity> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
            SkuInfoEntity skuInfoEntity = baseMapper.selectById(skuId);
            skuItemVo.setSkuInfo(skuInfoEntity);
            return skuInfoEntity;
        }, executor);

        //查询Spu对应的基本属性信息
        CompletableFuture<List<SkuItemAttrGroupVo>> groupVosFuture = skuInfoFuture.thenApplyAsync(res -> {
            List<SkuItemAttrGroupVo> itemAttrGroupVos = attrGroupService.getAttrGroupWithAttrBySpuId(res.getSpuId(), res.getCatalogId());
            skuItemVo.setGroupVos(itemAttrGroupVos);
            return itemAttrGroupVos;
        }, executor);

        //组装需要快速展示的数据
        CompletableFuture<Void> showDescFuture = groupVosFuture.thenAcceptAsync(res -> {
            List<SkuItemAttrGroupVo> groupVos = new ArrayList<>();
            for (SkuItemAttrGroupVo vo : res) {
                SkuItemAttrGroupVo groupVo = new SkuItemAttrGroupVo();
                List<SkuItemBaseAttrVo> collect = vo.getBaseAttrVos().stream()
                        .filter(attr -> attr.getShowDesc() != null && attr.getShowDesc() == 1)
                        .collect(Collectors.toList());
                groupVo.setGroupName("");
                groupVo.setBaseAttrVos(collect);
                groupVos.add(groupVo);
            }
            skuItemVo.setShowDesc(groupVos);
        }, executor);

        //查询指定的Spu介绍
        CompletableFuture<Void> spuInfoFuture = skuInfoFuture.thenAcceptAsync(res -> {
            SpuInfoDescEntity descEntity = spuInfoDescService.getById(res.getSpuId());
            skuItemVo.setDesc(descEntity);
        }, executor);

        //查询Spu包含的所有销售属性
        CompletableFuture<Void> saleAttrFuture = skuInfoFuture.thenAcceptAsync(res -> {
            List<SkuItemSaleAttrVo> saleAttrVos = skuSaleAttrValueService.getSaleAttrBySpuId(res.getSpuId());
            skuItemVo.setSaleAttrVos(saleAttrVos);
        }, executor);

        //查询品牌名
        CompletableFuture<Void> brandFuture = skuInfoFuture.thenAcceptAsync(res -> {
            BrandEntity brandEntity = brandService.getById(res.getBrandId());
            skuItemVo.setBrandName(brandEntity.getName());
        }, executor);

        //远程查询库存
        CompletableFuture<Void> skuHasStockFuture = CompletableFuture.runAsync(() -> {
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
        }, executor);

        //查询Sku对应的图片集合
        CompletableFuture<Void> skuImagesFuture = CompletableFuture.runAsync(() -> {
            List<SkuImagesEntity> skuImagesEntities = skuImagesService.getSkuImagesBySkuId(skuId);
            skuItemVo.setSkuImages(skuImagesEntities);
        }, executor);

        CompletableFuture.allOf(showDescFuture, spuInfoFuture, saleAttrFuture, brandFuture, skuHasStockFuture, skuImagesFuture).get();

        //未使用异步编排第一次访问：826ms，非第一次：22; 使用异步编排：590ms，非第一次：11
        //System.out.println(System.currentTimeMillis() - l);
        return skuItemVo;
    }
}
