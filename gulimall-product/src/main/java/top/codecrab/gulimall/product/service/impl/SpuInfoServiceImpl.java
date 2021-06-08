package top.codecrab.gulimall.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.codecrab.common.response.R;
import top.codecrab.common.to.SkuReductionTo;
import top.codecrab.common.to.SpuBoundsTo;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.Query;
import top.codecrab.gulimall.product.client.CouponFeignClient;
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
    private CouponFeignClient couponFeignClient;

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

}
