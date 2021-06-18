package top.codecrab.gulimall.product;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import top.codecrab.gulimall.product.entity.BrandEntity;
import top.codecrab.gulimall.product.service.AttrGroupService;
import top.codecrab.gulimall.product.service.BrandService;
import top.codecrab.gulimall.product.service.SkuSaleAttrValueService;
import top.codecrab.gulimall.product.web.vo.sku.SkuItemAttrGroupVo;
import top.codecrab.gulimall.product.web.vo.sku.SkuItemSaleAttrVo;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class GulimallProductApplicationTests {

    @Resource
    private BrandService brandService;

    @Resource
    private AttrGroupService attrGroupService;

    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedissonClient redisson;

    @Test
    void testSkuItem() {
        List<SkuItemAttrGroupVo> list = attrGroupService.getAttrGroupWithAttrBySpuId(3L, 225L);
        System.out.println(JSONUtil.toJsonStr(list));

        List<SkuItemSaleAttrVo> saleAttrVoList = skuSaleAttrValueService.getSaleAttrBySpuId(3L);
        System.out.println(JSONUtil.toJsonStr(saleAttrVoList));
    }

    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("小米");
        brandEntity.setLogo("https://pic.imgdb.cn/item/60b0482908f74bc1594d665d.jpg");
        brandEntity.setDescript("小米品牌");
        brandEntity.setShowStatus(1);
        brandEntity.setFirstLetter("x");
        brandEntity.setSort(0);

        boolean save = brandService.save(brandEntity);
        System.out.println("执行保存：" + save);
    }

    @Test
    void testRedisson() {
        System.out.println(redisson);
    }

    @Test
    void testRedis() {
        //每次操作都需要再次输入key
        //ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        //指定一个key，用这个取值或存值都不需要在输入key
        BoundValueOperations<String, Object> ops = redisTemplate.boundValueOps("hello");
        Hello hello = new Hello();
        hello.setName("Lucy");
        hello.setAge(18);
        hello.setBirthday(LocalDateTime.now());
        ops.set(hello);
        System.out.println("存入的对象：" + ops.get());
    }

    @Data
    static class Hello {
        private String name;
        private Integer age;
        private LocalDateTime birthday;
    }
}
