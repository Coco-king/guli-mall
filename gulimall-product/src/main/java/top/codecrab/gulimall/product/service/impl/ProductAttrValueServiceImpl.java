package top.codecrab.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.Query;
import top.codecrab.gulimall.product.dao.ProductAttrValueDao;
import top.codecrab.gulimall.product.entity.ProductAttrValueEntity;
import top.codecrab.gulimall.product.service.ProductAttrValueService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * spu属性值
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void updateBaseAddr(Long spuId, List<ProductAttrValueEntity> entities) {
        //先删除spu对应的属性
        baseMapper.delete(new QueryWrapper<ProductAttrValueEntity>()
                .eq("spu_id", spuId));

        //再添加
        List<ProductAttrValueEntity> collect = entities.stream()
                .peek(entity -> entity.setSpuId(spuId))
                .collect(Collectors.toList());
        this.saveBatch(collect);
    }

}
