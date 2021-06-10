package top.codecrab.gulimall.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.codecrab.common.constant.ProductConstant;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.Query;
import top.codecrab.gulimall.product.dao.AttrAttrgroupRelationDao;
import top.codecrab.gulimall.product.dao.AttrDao;
import top.codecrab.gulimall.product.dao.AttrGroupDao;
import top.codecrab.gulimall.product.entity.AttrAttrgroupRelationEntity;
import top.codecrab.gulimall.product.entity.AttrEntity;
import top.codecrab.gulimall.product.entity.AttrGroupEntity;
import top.codecrab.gulimall.product.entity.CategoryEntity;
import top.codecrab.gulimall.product.service.AttrService;
import top.codecrab.gulimall.product.service.CategoryService;
import top.codecrab.gulimall.product.vo.AttrRelationVo;
import top.codecrab.gulimall.product.vo.AttrResponseVo;
import top.codecrab.gulimall.product.vo.AttrVo;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商品属性
 *
 * @author codecrab
 * @date 2021-05-28 22:19:47
 */
@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Resource
    private AttrAttrgroupRelationDao relationDao;

    @Resource
    private AttrGroupDao attrGroupDao;

    @Resource
    private CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity = BeanUtil.copyProperties(attr, AttrEntity.class);
        baseMapper.insert(attrEntity);

        //如果是基本属性,保存关联信息
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_BASE.getCode() && attr.getAttrGroupId() != null) {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrId(attrEntity.getAttrId());
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationDao.insert(relationEntity);
        }
    }

    @Override
    public PageUtils queryBaseList(Map<String, Object> params, Long catelogId, String attrType) {
        boolean isBase = "base".equalsIgnoreCase(attrType);

        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();

        //设置类型匹配和模糊查询和id查询
        queryWrapper.eq("attr_type", isBase ? 1 : 0);

        String key = MapUtil.get(params, "key", String.class);
        if (StrUtil.isNotBlank(key)) {
            queryWrapper.and((wrapper) -> wrapper
                    .eq("attr_id", key).or()
                    .like("attr_name", key));
        }

        //分类id不为0才拼接条件
        queryWrapper.eq(catelogId != 0, "catelog_id", catelogId);

        IPage<AttrEntity> page = baseMapper.selectPage(
                new Query<AttrEntity>().getPage(params), queryWrapper
        );

        List<AttrEntity> records = page.getRecords();
        List<AttrResponseVo> voList = records.stream().map(attrEntity -> {
            AttrResponseVo responseVo = BeanUtil.copyProperties(attrEntity, AttrResponseVo.class);

            //如果是基本属性，就设置分组
            if (isBase) {
                AttrAttrgroupRelationEntity relationEntity = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>()
                        .eq("attr_id", responseVo.getAttrId()));
                if (relationEntity != null) {
                    AttrGroupEntity groupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
                    if (groupEntity != null) {
                        responseVo.setGroupName(groupEntity.getAttrGroupName());
                    }
                }
            }

            CategoryEntity categoryEntity = categoryService.getById(responseVo.getCatelogId());
            if (categoryEntity != null) {
                responseVo.setCatelogName(categoryEntity.getName());
            }
            return responseVo;
        }).collect(Collectors.toList());

        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(voList);
        return pageUtils;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAttr(AttrVo attr) {
        AttrEntity attrEntity = BeanUtil.copyProperties(attr, AttrEntity.class);
        baseMapper.updateById(attrEntity);

        //准备更新的数据
        AttrAttrgroupRelationEntity entity = new AttrAttrgroupRelationEntity();
        entity.setAttrGroupId(attr.getAttrGroupId());
        entity.setAttrId(attr.getAttrId());

        //更新中间表
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_BASE.getCode()) {
            Integer count = relationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq("attr_id", attr.getAttrId()));
            if (count > 0) {
                //存在，更新数据
                relationDao.update(entity, new UpdateWrapper<AttrAttrgroupRelationEntity>()
                        .eq("attr_id", attr.getAttrId()));
            } else {
                //不存在，新增
                relationDao.insert(entity);
            }
        }
    }

    @Override
    public AttrResponseVo getAttrResponseVo(Long attrId) {
        AttrEntity attrEntity = baseMapper.selectById(attrId);
        AttrResponseVo responseVo = BeanUtil.copyProperties(attrEntity, AttrResponseVo.class);

        //如果是基本属性,查询分组信息
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_BASE.getCode()) {
            AttrAttrgroupRelationEntity relationEntity = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq("attr_id", attrId));
            if (relationEntity != null) {
                responseVo.setAttrGroupId(relationEntity.getAttrGroupId());
            }
        }

        List<Long> catelogPath = categoryService.findCatelogPath(attrEntity.getCatelogId());
        responseVo.setCatelogPath(catelogPath);
        return responseVo;
    }

    @Override
    public List<AttrEntity> findAttrRelation(Long attrGroupId) {
        List<AttrAttrgroupRelationEntity> entities = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .eq("attr_group_id", attrGroupId));

        if (CollectionUtil.isEmpty(entities)) {
            return Collections.emptyList();
        }

        List<Long> collect = entities.stream()
                .map(AttrAttrgroupRelationEntity::getAttrId)
                .collect(Collectors.toList());

        return baseMapper.selectBatchIds(collect);
    }

    @Override
    public void removeRelations(List<AttrRelationVo> relationVos) {
        QueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = new QueryWrapper<>();

        for (AttrRelationVo relationVo : relationVos) {
            queryWrapper.or(wrapper -> wrapper
                    .eq("attr_id", relationVo.getAttrId())
                    .eq("attr_group_id", relationVo.getAttrGroupId()));
        }

        relationDao.delete(queryWrapper);
    }

    @Override
    public PageUtils findAttrNoRelation(Map<String, Object> params, Long attrGroupId) {
        //获得当前分组所属的分类id
        AttrGroupEntity groupEntity = attrGroupDao.selectById(attrGroupId);
        Long catelogId = groupEntity.getCatelogId();

        //获取分类id下的所有分组
        List<AttrGroupEntity> entityList = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>()
                .eq("catelog_id", catelogId));
        //提取出属性分组ID
        List<Long> attrGroupIds = entityList.stream()
                .map(AttrGroupEntity::getAttrGroupId)
                .collect(Collectors.toList());

        //查询出关联表中所关联的数据
        List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .in("attr_group_id", attrGroupIds));
        //提取出关联信息的attrId
        List<Long> attrIds = relationEntities.stream()
                .map(AttrAttrgroupRelationEntity::getAttrId)
                .collect(Collectors.toList());

        //查询出不包含attrIds的所有指定分类下的数据
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>()
                .eq("catelog_id", catelogId)
                .eq("attr_type", ProductConstant.AttrEnum.ATTR_BASE.getCode())
                .notIn(CollectionUtil.isNotEmpty(attrIds), "attr_id", attrIds);

        String key = MapUtil.get(params, "key", String.class);
        if (StrUtil.isNotBlank(key)) {
            queryWrapper.eq("attr_id", key).or()
                    .like("attr_name", key);
        }

        IPage<AttrEntity> page = baseMapper.selectPage(
                new Query<AttrEntity>().getPage(params), queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public Set<Long> getSearchAttrIds(Set<Long> attrIds) {
        return baseMapper.selectSearchAttrIds(attrIds);
    }

}
