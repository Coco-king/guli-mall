package top.codecrab.gulimall.product.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.Query;
import top.codecrab.gulimall.product.dao.SpuImagesDao;
import top.codecrab.gulimall.product.entity.SpuImagesEntity;
import top.codecrab.gulimall.product.service.SpuImagesService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * spu图片
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuImagesEntity> page = this.page(
                new Query<SpuImagesEntity>().getPage(params),
                new QueryWrapper<SpuImagesEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSpuImages(Long spuId, List<String> images) {
        if (CollectionUtil.isNotEmpty(images)) {

            List<SpuImagesEntity> collect = images.stream().map(img -> {
                SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
                spuImagesEntity.setSpuId(spuId);
                spuImagesEntity.setImgName(img.substring(img.lastIndexOf("/") + 1));
                spuImagesEntity.setImgUrl(img);
                spuImagesEntity.setImgSort(0);
                spuImagesEntity.setDefaultImg(0);
                return spuImagesEntity;
            }).collect(Collectors.toList());

            this.saveBatch(collect);
        }
    }

}
