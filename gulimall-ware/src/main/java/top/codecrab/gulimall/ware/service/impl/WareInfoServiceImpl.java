package top.codecrab.gulimall.ware.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.Query;
import top.codecrab.gulimall.ware.dao.WareInfoDao;
import top.codecrab.gulimall.ware.entity.WareInfoEntity;
import top.codecrab.gulimall.ware.service.WareInfoService;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author codecrab
 * @date 2021-05-28 22:50:44
 */
@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = MapUtil.getStr(params, "key");

        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                new QueryWrapper<WareInfoEntity>()
                        .and(StrUtil.isNotBlank(key), wrapper -> wrapper
                                .eq("id", key)
                                .or().like("name", key)
                                .or().like("address", key)
                                .or().like("areacode", key)
                        )
        );

        return new PageUtils(page);
    }

}
