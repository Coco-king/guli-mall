package top.codecrab.gulimall.product.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.codecrab.common.constant.RedisConstant;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.Query;
import top.codecrab.gulimall.product.dao.CategoryDao;
import top.codecrab.gulimall.product.entity.CategoryBrandRelationEntity;
import top.codecrab.gulimall.product.entity.CategoryEntity;
import top.codecrab.gulimall.product.service.CategoryBrandRelationService;
import top.codecrab.gulimall.product.service.CategoryService;
import top.codecrab.gulimall.product.web.vo.Catalog2Vo;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 商品三级分类
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> entities = baseMapper.selectList(null);

        return entities.stream()
                .filter(item -> item.getParentCid() == 0)
                .peek(menu -> menu.setChildren(this.getChildren(menu, entities)))
                .sorted(Comparator.comparingInt(sort -> (sort.getSort() == null ? 0 : sort.getSort())))
                .collect(Collectors.toList());
    }

    @Override
    public void removeMenuByIds(List<Long> ids) {
        //TODO 删除前判断系统中是否正在使用该菜单

        //逻辑删除
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public List<Long> findCatelogPath(Long catelogId) {
        List<Long> path = this.findParentPath(catelogId, new ArrayList<>());
        Collections.reverse(path);
        return path;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDetail(CategoryEntity category) {
        baseMapper.updateById(category);

        categoryBrandRelationService.update(
                new UpdateWrapper<CategoryBrandRelationEntity>()
                        .set("catelog_name", category.getName())
                        .eq("catelog_id", category.getCatId())
        );
    }

    @Override
    public List<CategoryEntity> getLevel1Categories() {
        return baseMapper.selectList(new QueryWrapper<CategoryEntity>()
                .eq("parent_cid", 0));
    }

    /**
     * 使用Redis的几个问题：
     * 1、缓存穿透：
     * 原因：当大量请求查询一个一定不存在的数据时，就会全部发给数据库，导致数据库崩溃
     * 解决方案：空结果缓存
     * 2、缓存雪崩：
     * 原因：当大面积的key同时失效，造成大量请求全部返回空值，导致去查询数据库，造成数据库崩溃
     * 解决方案：在预先过期的时间的基础上添加一个短期的随机时间，使这些key不在同一时间失效
     * 3、缓存击穿：
     * 原因：当大量请求同时访问一个热点key的时候，这个key突然过期，这些请求同时去查询数据库，导致数据库崩溃
     * 解决方案：设置一个锁，当大量请求同时进来时，第一个请求拿到锁，去查询数据库并放入缓存。后面的请求拿到锁之后，缓存中已经有数据了
     */
    @Override
    public Object getCatalogJson() {
        BoundValueOperations<String, Object> ops = redisTemplate.boundValueOps(RedisConstant.CategoryConstant.REDIS_KEY);

        Object result = ops.get();
        //redis中没有找到，就查询数据库，否则直接返回
        if (result == null) {
            //如果是分布式，那么有几个本服务，那么请求就会进来几个，所以需要使用分布式锁
            return getCatalogJsonFormDbWithRedisLock(ops);
        }
        System.out.println("从缓存中获取...");
        return result;
    }

    private Object getCatalogJsonFormDbWithRedisLock(BoundValueOperations<String, Object> ops) {
        ValueOperations<String, Object> lock = redisTemplate.opsForValue();
        //由于业务时间很长，如果锁过期了，别的线程进来之后又拿到了锁，然后我们一删就把别的线程的锁删掉了，所以准备一个占锁的UUID，每个人匹配是自己的锁才删除
        String uuid = UUID.fastUUID().toString();
        /*
        设置锁，当指定key不存在的时候才会插入并返回true，表示占锁成功
        这一步设置删除时间的目的是当业务加完锁之后出现异常或是服务器宕机，lock锁不会锁死
        这一步必须是原子性的，也就是一步完成，否则中途服务器宕机还是会导致过期时间设置不上
        */
        Boolean absent = lock.setIfAbsent(RedisConstant.REDIS_LOCK, uuid, 300, TimeUnit.SECONDS);

        //表示占锁成功
        if (absent != null && absent) {
            System.out.println("获取分布式锁成功...");
            Object data;
            try {
                data = this.getDataFromDbOrCache(ops);
            } finally {
                /*
                释放锁（删除lock），如果正好判断是当前值，正要删除锁的时候，锁已经过期，别人已经设置到了新的值。
                那么我们删除的是别人的锁这一步也必须是原子性的，那么就必须使用redis+Lua脚本完成
                如果传入的key=value，就删除他，并返回 0或1，每个人匹配是自己的锁才删除
                TODO 更难的事情，锁的自动续期，当前业务没有执行完毕，锁过期了怎么办，最简单可以吧过期时间设置长点
                */
                String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
                redisTemplate.execute(
                        new DefaultRedisScript<>(script, Long.class),
                        Collections.singletonList(RedisConstant.REDIS_LOCK),
                        uuid
                );
            }
            return data;
        } else {
            System.out.println("获取分布式锁失败...等待重试...");
            //此处可以选择线程睡眠100ms，等待拿到锁的线程的业务逻辑执行完毕
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return this.getCatalogJsonFormDbWithRedisLock(ops);
        }
    }

    /**
     * 本地锁
     */
    private Object getCatalogJsonFormDbWithLocalLock(BoundValueOperations<String, Object> ops) {
        //如果缓存为空，添加本地锁，加锁防止缓存击穿
        //如果是分布式，那么有几个本服务，那么请求就会进来几个，所以需要使用分布式锁
        synchronized (this) {
            return getDataFromDbOrCache(ops);
        }
    }

    private Object getDataFromDbOrCache(BoundValueOperations<String, Object> ops) {
        //因为上个对象释放锁之后已经查询了数据库，所以再次查询缓存，确定缓存中没有值
        Object again = ops.get();
        if (again != null) {
            System.out.println("缓存击穿成功预防。加锁代码块中查询缓存命中(正确)...");
            return again;
        }

        System.out.println("缓存中没有...查询数据库...(理想状态打印一次)");
        Map<String, List<Catalog2Vo>> jsonFormDb = this.getCatalogJsonFormDb();
        if (MapUtil.isEmpty(jsonFormDb)) {
            //如果为空就设置空映射，防止缓存穿透
            ops.set(RedisConstant.REDIS_NULL_MAP, 30, TimeUnit.SECONDS);
        } else {
            int oneDay = 86400;
            //随机时间 5-10分钟，解决缓存雪崩
            int randomTime = RandomUtil.randomInt(300, 600);
            ops.set(jsonFormDb, oneDay + randomTime, TimeUnit.SECONDS);
        }
        return jsonFormDb;
    }

    private Map<String, List<Catalog2Vo>> getCatalogJsonFormDb() {
        List<CategoryEntity> list = baseMapper.selectList(Wrappers.emptyWrapper());
        List<CategoryEntity> categories = this.getChildrenByParentCid(list, 0L);

        return categories.stream().collect(Collectors.toMap(
                //key为一级分类的id
                l1 -> l1.getCatId().toString(),
                //value通过一级分类id查询出他的所有子分类，并封装为Catalog2Vo
                l1 -> this.getChildrenByParentCid(list, l1.getCatId()).stream()
                        .map(l2 -> {
                            //通过二级分类id查询出他的所有子分类，并封装为Catalog3Vo
                            List<Catalog2Vo.Catalog3Vo> catalog3Vos = this.getChildrenByParentCid(list, l2.getCatId()).stream()
                                    .map(l3 -> new Catalog2Vo.Catalog3Vo(
                                            l2.getCatId().toString(),
                                            l3.getCatId().toString(),
                                            l3.getName()
                                    ))
                                    .collect(Collectors.toList());
                            //封装返回Catalog2Vo
                            return new Catalog2Vo(
                                    l1.getCatId().toString(),
                                    catalog3Vos,
                                    l2.getCatId().toString(),
                                    l2.getName()
                            );
                        })
                        .collect(Collectors.toList())
                )
        );
    }

    /**
     * 从给定的集合中过滤出指定父id的子分类
     */
    private List<CategoryEntity> getChildrenByParentCid(List<CategoryEntity> list, Long parentCid) {
        return list.stream().filter(item -> item.getParentCid().equals(parentCid)).collect(Collectors.toList());
    }

    /**
     * 拼装指定id的父id路径 [2,25,255]
     */
    private List<Long> findParentPath(Long catelogId, List<Long> path) {
        //收集当前id
        path.add(catelogId);
        CategoryEntity categoryEntity = baseMapper.selectById(catelogId);
        if (categoryEntity.getParentCid() != 0) {
            this.findParentPath(categoryEntity.getParentCid(), path);
        }
        return path;
    }

    /**
     * 从指定列表中找到root的子列表
     *
     * @param all  所有数据列表
     * @param root 指定的节点
     */
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {
        return all.stream()
                .filter(item -> item.getParentCid().equals(root.getCatId()))
                .peek(menu -> menu.setChildren(this.getChildren(menu, all)))
                .sorted(Comparator.comparingInt(sort -> (sort.getSort() == null ? 0 : sort.getSort())))
                .collect(Collectors.toList());
    }
}
