package top.codecrab.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.codecrab.common.constant.WareConstant;
import top.codecrab.common.exception.RRException;
import top.codecrab.common.response.ErrorCodeEnum;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.Query;
import top.codecrab.gulimall.ware.dao.PurchaseDao;
import top.codecrab.gulimall.ware.entity.PurchaseDetailEntity;
import top.codecrab.gulimall.ware.entity.PurchaseEntity;
import top.codecrab.gulimall.ware.service.PurchaseDetailService;
import top.codecrab.gulimall.ware.service.PurchaseService;
import top.codecrab.gulimall.ware.service.WareSkuService;
import top.codecrab.gulimall.ware.vo.MergeVo;
import top.codecrab.gulimall.ware.vo.PurchaseDoneItemsVo;
import top.codecrab.gulimall.ware.vo.PurchaseDoneVo;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 采购信息
 *
 * @author codecrab
 * @date 2021-05-28 22:50:44
 */
@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Resource
    private PurchaseDetailService purchaseDetailService;

    @Resource
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnReceiveList(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
                        .eq("status", 0)
                        .or()
                        .eq("status", 1)
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void merge(MergeVo mergeVo) {
        PurchaseEntity select = baseMapper.selectById(mergeVo.getPurchaseId());

        boolean isAdd = select == null;
        //如果没有传purchaseId，或未找到 就新增一个采购单
        if (isAdd) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setPriority(1);
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            purchaseEntity.setCreateTime(LocalDateTime.now());
            purchaseEntity.setUpdateTime(purchaseEntity.getCreateTime());
            baseMapper.insert(purchaseEntity);
            mergeVo.setPurchaseId(purchaseEntity.getId());
        } else if (
            //如果采购单不为空，并且采购单的状态不是新建或已分配，那么就抛出异常
                select.getStatus() != WareConstant.PurchaseStatusEnum.CREATED.getCode() &&
                        select.getStatus() != WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()
        ) {
            throw new RRException(ErrorCodeEnum.PURCHASE_STATUS_NOT_CREATED_OR_ASSIGNED);
        }

        List<PurchaseDetailEntity> collect = mergeVo.getItems().stream()
                .map(item -> {
                    PurchaseDetailEntity entity = purchaseDetailService.getById(item);
                    if (entity == null) {
                        throw new RRException(ErrorCodeEnum.PURCHASE_DETAIL_NOT_FIND);
                    } else if (
                        //如果采购需求不为空，并且采购需求的状态不是新建或已分配，那么就抛出异常
                            entity.getStatus() != WareConstant.PurchaseDetailStatusEnum.CREATED.getCode() &&
                                    entity.getStatus() != WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode()
                    ) {
                        throw new RRException(ErrorCodeEnum.PURCHASE_DETAIL_STATUS_NOT_CREATED_OR_ASSIGNED);
                    }

                    PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
                    detailEntity.setId(item);
                    detailEntity.setPurchaseId(mergeVo.getPurchaseId());
                    detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
                    return detailEntity;
                })
                .collect(Collectors.toList());
        purchaseDetailService.updateBatchById(collect);

        //如果不是增加采购单，就修改更新时间
        if (!isAdd) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setId(mergeVo.getPurchaseId());
            purchaseEntity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(purchaseEntity);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void received(List<Long> purchaseIds) {
        //根据id构建领取后的采购单数据列表
        List<PurchaseEntity> collect = purchaseIds.stream()
                .map(id -> baseMapper.selectById(id))
                .filter(purchase ->
                        purchase.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() ||
                                purchase.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()
                )
                .peek(purchase -> {
                    purchase.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
                    purchase.setUpdateTime(LocalDateTime.now());
                })
                .collect(Collectors.toList());

        //更新采购单列表
        this.updateBatchById(collect);

        //更新与之关联的采购需求
        collect.forEach(purchase -> {
            //查询与采购单关联的采购需求
            List<PurchaseDetailEntity> entities = purchaseDetailService.listByPurchaseId(purchase.getId());

            //修改他们的状态
            List<PurchaseDetailEntity> purchaseDetailEntities = entities.stream()
                    .map(purchaseDetail -> {
                        PurchaseDetailEntity entity = new PurchaseDetailEntity();
                        entity.setId(purchaseDetail.getId());
                        entity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                        return entity;
                    })
                    .collect(Collectors.toList());

            purchaseDetailService.updateBatchById(purchaseDetailEntities);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void done(PurchaseDoneVo doneVo) {
        boolean isSuccess = true;
        List<PurchaseDetailEntity> updates = new ArrayList<>();

        for (PurchaseDoneItemsVo item : doneVo.getItems()) {
            Long itemId = item.getItemId();
            PurchaseDetailEntity entity = purchaseDetailService.getById(itemId);
            if (entity.getStatus() != WareConstant.PurchaseDetailStatusEnum.BUYING.getCode()) {
                throw new RRException(ErrorCodeEnum.PURCHASE_DETAIL_STATUS_MUST_BE_BUYING);
            }

            PurchaseDetailEntity now = new PurchaseDetailEntity();
            if (item.getStatus() == WareConstant.PurchaseDetailStatusEnum.HAS_ERROR.getCode()) {
                isSuccess = false;
                now.setStatus(item.getStatus());
            } else if (item.getStatus() == WareConstant.PurchaseDetailStatusEnum.FINISH.getCode()) {
                //把成功采购的进行入库
                wareSkuService.updateStock(entity.getSkuId(), entity.getWareId(), entity.getSkuNum());

                now.setStatus(item.getStatus());
            } else {
                throw new RRException(ErrorCodeEnum.PURCHASE_DETAIL_STATUS_UNKNOWN);
            }
            now.setId(itemId);
            updates.add(now);
        }
        purchaseDetailService.updateBatchById(updates);

        //改变采购单状态
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(doneVo.getId());
        purchaseEntity.setStatus(isSuccess ?
                WareConstant.PurchaseDetailStatusEnum.FINISH.getCode() :
                WareConstant.PurchaseDetailStatusEnum.HAS_ERROR.getCode()
        );
        purchaseEntity.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(purchaseEntity);
    }

}
