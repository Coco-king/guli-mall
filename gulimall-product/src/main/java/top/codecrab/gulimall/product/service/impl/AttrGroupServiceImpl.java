package top.codecrab.gulimall.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
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
import top.codecrab.gulimall.product.service.AttrService;
import top.codecrab.gulimall.product.vo.AttrGroupWithAttrsVo;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 属性分组
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Resource
    private AttrService attrService;

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

    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrByCatelogId(Long catelogId) {
        List<AttrGroupEntity> groupEntities = baseMapper.selectList(new QueryWrapper<AttrGroupEntity>()
                .eq("catelog_id", catelogId));

        return groupEntities.stream().map(group -> {
            AttrGroupWithAttrsVo vo = BeanUtil.copyProperties(group, AttrGroupWithAttrsVo.class);
            vo.setAttrs(attrService.findAttrRelation(vo.getAttrGroupId()));
            return vo;
        }).collect(Collectors.toList());
    }

}
