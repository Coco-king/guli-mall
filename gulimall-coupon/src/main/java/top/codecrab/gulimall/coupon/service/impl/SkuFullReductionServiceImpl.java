package top.codecrab.gulimall.coupon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.codecrab.common.to.SkuReductionTo;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.Query;
import top.codecrab.gulimall.coupon.dao.SkuFullReductionDao;
import top.codecrab.gulimall.coupon.entity.MemberPriceEntity;
import top.codecrab.gulimall.coupon.entity.SkuFullReductionEntity;
import top.codecrab.gulimall.coupon.entity.SkuLadderEntity;
import top.codecrab.gulimall.coupon.service.MemberPriceService;
import top.codecrab.gulimall.coupon.service.SkuFullReductionService;
import top.codecrab.gulimall.coupon.service.SkuLadderService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品满减信息
 *
 * @author codecrab
 * @date 2021-05-28 22:26:50
 */
@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Resource
    private SkuLadderService skuLadderService;

    @Resource
    private MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * sku的满减、优惠信息 gulimall_sms -> sms_sku_ladder / sms_sku_full_reduction / sms_member_price
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInfo(SkuReductionTo skuReductionTo) {
        // sms_sku_ladder
        if (skuReductionTo.getFullCount() > 0) {
            SkuLadderEntity skuLadderEntity = BeanUtil.copyProperties(skuReductionTo, SkuLadderEntity.class);
            skuLadderEntity.setAddOther(1);
            skuLadderService.save(skuLadderEntity);
        }

        if (skuReductionTo.getFullPrice().compareTo(BigDecimal.ZERO) > 0) {
            // sms_sku_full_reduction
            SkuFullReductionEntity skuFullReductionEntity = BeanUtil.copyProperties(skuReductionTo, SkuFullReductionEntity.class);
            skuFullReductionEntity.setAddOther(1);
            baseMapper.insert(skuFullReductionEntity);
        }

        List<MemberPriceEntity> memberPriceEntities = skuReductionTo.getMemberPrice().stream()
                .filter(memberPriceVo -> memberPriceVo.getPrice().compareTo(BigDecimal.ZERO) > 0)
                .map(memberPriceVo -> {
                    MemberPriceEntity priceEntity = new MemberPriceEntity();
                    priceEntity.setSkuId(skuReductionTo.getSkuId());
                    priceEntity.setMemberLevelId(memberPriceVo.getId());
                    priceEntity.setMemberLevelName(memberPriceVo.getName());
                    priceEntity.setMemberPrice(memberPriceVo.getPrice());
                    priceEntity.setAddOther(1);
                    return priceEntity;
                })
                .collect(Collectors.toList());

        memberPriceService.saveBatch(memberPriceEntities);
    }

}
