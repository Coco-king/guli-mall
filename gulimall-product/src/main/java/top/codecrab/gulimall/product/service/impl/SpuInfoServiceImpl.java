package top.codecrab.gulimall.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.codecrab.common.constant.ProductConstant;
import top.codecrab.common.response.R;
import top.codecrab.common.to.SkuHasStockTo;
import top.codecrab.common.to.SkuReductionTo;
import top.codecrab.common.to.SpuBoundsTo;
import top.codecrab.common.to.es.SkuEsModel;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.Query;
import top.codecrab.gulimall.product.client.CouponFeignClient;
import top.codecrab.gulimall.product.client.SearchFeignClient;
import top.codecrab.gulimall.product.client.WareFeignClient;
import top.codecrab.gulimall.product.dao.SpuInfoDao;
import top.codecrab.gulimall.product.entity.*;
import top.codecrab.gulimall.product.service.*;
import top.codecrab.gulimall.product.vo.spu.BoundsVo;
import top.codecrab.gulimall.product.vo.spu.ImagesVo;
import top.codecrab.gulimall.product.vo.spu.SkusVo;
import top.codecrab.gulimall.product.vo.spu.SpuSaveVo;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * spu信息
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Resource
    private SpuInfoDescService spuInfoDescService;

    @Resource
    private SpuImagesService spuImagesService;

    @Resource
    private AttrService attrService;

    @Resource
    private ProductAttrValueService productAttrValueService;

    @Resource
    private SkuInfoService skuInfoService;

    @Resource
    private SkuImagesService skuImagesService;

    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Resource
    private BrandService brandService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private CouponFeignClient couponFeignClient;

    @Resource
    private WareFeignClient wareFeignClient;

    @Resource
    private SearchFeignClient searchFeignClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = MapUtil.getStr(params, "key");
        String status = MapUtil.getStr(params, "status");
        String brandId = MapUtil.getStr(params, "brandId");
        String catelogId = MapUtil.getStr(params, "catelogId");

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
                        .eq(StrUtil.isNotBlank(status), "publish_status", status)
                        .eq(StrUtil.isNotBlank(brandId) && !"0".equals(brandId), "brand_id", brandId)
                        .eq(StrUtil.isNotBlank(catelogId) && !"0".equals(catelogId), "catalog_id", catelogId)
                        .and(StrUtil.isNotBlank(key), wrapper -> wrapper
                                .eq("id", key).or()
                                .like("spu_name", key)
                        )
        );

        return new PageUtils(page);
    }

    /**
     * TODO 高级部分完善失败情况下的处理
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSpuInfo(SpuSaveVo saveVo) {
        //1、保存spu的基本信息 pms_spu_info
        SpuInfoEntity spuInfoEntity = BeanUtil.copyProperties(saveVo, SpuInfoEntity.class);
        spuInfoEntity.setPublishStatus(ProductConstant.StatusEnum.NEW_SPU.getCode());
        spuInfoEntity.setCreateTime(LocalDateTime.now());
        spuInfoEntity.setUpdateTime(spuInfoEntity.getCreateTime());
        baseMapper.insert(spuInfoEntity);
        Long spuId = spuInfoEntity.getId();

        //2、保存spu的描述图片 pms_spu_info_desc
        List<String> decrypt = saveVo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuId);
        spuInfoDescEntity.setDecript(String.join(",", decrypt));
        spuInfoDescService.save(spuInfoDescEntity);

        //3、保存spu的图片集 pms_spu_images
        spuImagesService.saveSpuImages(spuId, saveVo.getImages());

        //4、保存spu的规格参数 pms_product_attr_value
        List<ProductAttrValueEntity> attrValueEntities = saveVo.getBaseAttrs().stream().map(baseAttrsVo -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setSpuId(spuId);
            productAttrValueEntity.setAttrId(baseAttrsVo.getAttrId());

            //查出属性
            AttrEntity attrEntity = attrService.getById(baseAttrsVo.getAttrId());
            productAttrValueEntity.setAttrName(attrEntity.getAttrName());
            productAttrValueEntity.setAttrValue(baseAttrsVo.getAttrValues());
            productAttrValueEntity.setAttrSort(0);
            productAttrValueEntity.setQuickShow(baseAttrsVo.getShowDesc());

            return productAttrValueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveBatch(attrValueEntities);

        //5、保存spu的积分信息 gulimall_sms -> sms_spu_bounds
        BoundsVo bounds = saveVo.getBounds();
        SpuBoundsTo boundsTo = BeanUtil.copyProperties(bounds, SpuBoundsTo.class);
        boundsTo.setSpuId(spuId);
        R spuBoundsRes = couponFeignClient.saveSpuBounds(boundsTo);
        if (spuBoundsRes.getCode() != 0) {
            log.error("======= 远程调用保存spu积分信息失败 =======");
        }

        //6、保存spu的sku信息
        //6.1) sku的基本信息 pms_sku_info
        List<SkusVo> skus = saveVo.getSkus();
        if (CollectionUtil.isNotEmpty(skus)) {
            skus.forEach(sku -> {
                String defaultImg = "";

                List<ImagesVo> images = sku.getImages();
                for (ImagesVo image : images) {
                    if (image.getDefaultImg() == 1) {
                        defaultImg = image.getImgUrl();
                    }
                }

                SkuInfoEntity skuInfoEntity = BeanUtil.copyProperties(sku, SkuInfoEntity.class);
                skuInfoEntity.setSpuId(spuId);
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoEntity.setSaleCount(0L);
                skuInfoService.save(skuInfoEntity);
                Long skuId = skuInfoEntity.getSkuId();

                //6.2) sku的图片信息 pms_sku_images
                List<SkuImagesEntity> collect = images.stream()
                        .filter(img -> StrUtil.isNotBlank(img.getImgUrl()))
                        .map(img -> {
                            SkuImagesEntity skuImagesEntity = BeanUtil.copyProperties(img, SkuImagesEntity.class);
                            skuImagesEntity.setSkuId(skuId);
                            skuImagesEntity.setImgSort(0);
                            return skuImagesEntity;
                        })
                        .collect(Collectors.toList());
                skuImagesService.saveBatch(collect);

                //6.3) sku的销售属性信息 pms_sku_sale_attr_value
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = sku.getAttr().stream().map(attrVo -> {
                    SkuSaleAttrValueEntity entity = BeanUtil.copyProperties(attrVo, SkuSaleAttrValueEntity.class);
                    entity.setSkuId(skuId);
                    entity.setAttrSort(0);
                    return entity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

                //6.4) sku的满减、优惠信息 gulimall_sms -> sms_sku_ladder / sms_sku_full_reduction / sms_member_price
                if (sku.getFullCount() > 0 || sku.getFullPrice().compareTo(BigDecimal.ZERO) > 0) {
                    SkuReductionTo skuReductionTo = BeanUtil.copyProperties(sku, SkuReductionTo.class);
                    skuReductionTo.setSkuId(skuId);
                    R skuReductionRes = couponFeignClient.saveSkuReduction(skuReductionTo);
                    if (skuReductionRes.getCode() != 0) {
                        log.error("======= 远程调用保存sku优惠信息失败 =======");
                    }
                }
            });
        }
    }

    @Override
    public void up(Long spuId) {

        List<SkuInfoEntity> skus = skuInfoService.list(new QueryWrapper<SkuInfoEntity>()
                .eq("spu_id", spuId));

        if (CollectionUtil.isNotEmpty(skus)) {
            // TODO 每一个sku的品牌和分类都是一样的，查询品牌和分类信息
            BrandEntity brandEntity = brandService.getById(skus.get(0).getBrandId());
            CategoryEntity categoryEntity = categoryService.getById(skus.get(0).getCatalogId());

            // 通过spuId找到所有可被检索的属性信息
            List<ProductAttrValueEntity> baseAttrs = productAttrValueService.getBaseAttrBySpuId(spuId);

            Set<Long> collect = baseAttrs.stream()
                    .map(ProductAttrValueEntity::getAttrId)
                    .collect(Collectors.toSet());

            //可以被检索的 属性id集合
            Set<Long> attrIds = attrService.getSearchAttrIds(collect);

            List<SkuEsModel.Attrs> attrs = baseAttrs.stream()
                    //过滤出attrId包含在attrIds里面的数据
                    .filter(attr -> attrIds.contains(attr.getAttrId()))
                    //对象拷贝到SkuEsModel.Attrs里
                    .map(attr -> BeanUtil.copyProperties(attr, SkuEsModel.Attrs.class))
                    .collect(Collectors.toList());

            List<Long> skuIds = skus.stream()
                    .map(SkuInfoEntity::getSkuId)
                    .collect(Collectors.toList());

            Map<Long, Boolean> hasStock = null;
            // 远程调用查看是否有库存
            try {
                R r = wareFeignClient.hasStock(skuIds);
                // feignData  List<Map<Integer, Boolean>>
                Object feignData = r.getFeignData();
                if (r.getCode() == 0 && feignData != null) {
                    List<SkuHasStockTo> stock = r.getFeignData(new TypeReference<List<SkuHasStockTo>>() {
                    });
                    hasStock = stock.stream().collect(Collectors.toMap(SkuHasStockTo::getSkuId, SkuHasStockTo::getHasStock));
                }
            } catch (Exception e) {
                log.error("========= 远程调用gulimall-ware的hasStock方法失败 =========", e);
            }

            Map<Long, Boolean> finalHasStock = hasStock;
            List<SkuEsModel> modelList = skus.stream().map(
                    sku -> {
                        SkuEsModel skuEsModel = BeanUtil.copyProperties(sku, SkuEsModel.class);
                        skuEsModel.setSkuPrice(sku.getPrice());
                        skuEsModel.setSkuImg(sku.getSkuDefaultImg());

                        //TODO 默认0。后期优化
                        skuEsModel.setHotScore(0L);

                        //如果远程调用失败就默认有库存
                        if (finalHasStock == null) {
                            skuEsModel.setHasStock(true);
                        } else {
                            skuEsModel.setHasStock(finalHasStock.get(sku.getSkuId()));
                        }

                        //设置品牌和分类信息
                        skuEsModel.setBrandName(brandEntity.getName());
                        skuEsModel.setBrandImg(brandEntity.getLogo());
                        skuEsModel.setCatalogName(categoryEntity.getName());

                        //设置可以被检索的属性
                        skuEsModel.setAttrs(attrs);
                        return skuEsModel;
                    }
            ).collect(Collectors.toList());

            try {
                // 发送给ES进行保存
                R r = searchFeignClient.produceStatusUp(modelList);
                if (r.getCode() == 0) {
                    //远程调用成功，修改spu的状态
                    baseMapper.updateStatus(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
                } else {
                    //远程调用失败
                    //TODO 重复调用问题，接口幂等性？重试机制
                }
            } catch (Exception e) {
                log.error("远程调用保存ES失败", e);
            }
        }
    }

}
