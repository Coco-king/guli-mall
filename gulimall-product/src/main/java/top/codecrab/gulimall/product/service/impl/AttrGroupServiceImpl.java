package top.codecrab.gulimall.product.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.Query;
import top.codecrab.gulimall.product.dao.AttrGroupDao;
import top.codecrab.gulimall.product.entity.AttrGroupEntity;
import top.codecrab.gulimall.product.service.AttrGroupService;
import top.codecrab.gulimall.product.vo.AttrRelationVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        String key = MapUtil.get(params, "key", String.class);

        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();

        if (StrUtil.isNotBlank(key)) {
            queryWrapper.and((wrapper ->
                    wrapper.eq("attr_group_id", key)
                            .or().like("attr_group_name", key)
            ));
        }

        queryWrapper.eq(catelogId != 0, "catelog_id", catelogId);

        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params), queryWrapper
        );
        return new PageUtils(page);
    }

}
