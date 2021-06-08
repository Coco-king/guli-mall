package top.codecrab.gulimall.ware.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.Query;
import top.codecrab.gulimall.ware.dao.PurchaseDetailDao;
import top.codecrab.gulimall.ware.entity.PurchaseDetailEntity;
import top.codecrab.gulimall.ware.service.PurchaseDetailService;

import java.util.List;
import java.util.Map;

/**
 * @author codecrab
 * @date 2021-05-28 22:50:44
 */
@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = MapUtil.getStr(params, "key");
        String status = MapUtil.getStr(params, "status");
        String wareId = MapUtil.getStr(params, "wareId");

        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                new QueryWrapper<PurchaseDetailEntity>()
                        .eq(StrUtil.isNotBlank(status), "status", status)
                        .eq(StrUtil.isNotBlank(wareId) && !"0".equals(wareId), "ware_id", wareId)
                        .and(StrUtil.isNotBlank(key), wrapper -> wrapper
                                .eq("purchase_id", key).or()
                                .eq("sku_id", key)
                        )
        );

        return new PageUtils(page);
    }

    @Override
    public List<PurchaseDetailEntity> listByPurchaseId(Long purchaseId) {
        return baseMapper.selectList(new QueryWrapper<PurchaseDetailEntity>()
                .eq("purchase_id", purchaseId));
    }
}
