package top.codecrab.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.codecrab.common.to.SkuHasStockTo;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.gulimall.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author codecrab
 * @date 2021-05-28 22:50:44
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 更新库存
     */
    void updateStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockTo> getSkusHasStock(List<Long> skuIds);
}

