package top.codecrab.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.gulimall.ware.entity.PurchaseEntity;
import top.codecrab.gulimall.ware.vo.MergeVo;
import top.codecrab.gulimall.ware.vo.PurchaseDoneVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author codecrab
 * @date 2021-05-28 22:50:44
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnReceiveList(Map<String, Object> params);

    void merge(MergeVo mergeVo);

    void received(List<Long> purchaseIds);

    void done(PurchaseDoneVo doneVo);
}

