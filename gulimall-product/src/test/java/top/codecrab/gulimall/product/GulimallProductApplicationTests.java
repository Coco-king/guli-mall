package top.codecrab.gulimall.product;

import cn.hutool.core.lang.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import top.codecrab.gulimall.product.entity.BrandEntity;
import top.codecrab.gulimall.product.service.BrandService;

import javax.annotation.Resource;

@SpringBootTest
class GulimallProductApplicationTests {

    @Resource
    private BrandService brandService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

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
    void testRedis() {
        //每次操作都需要再次输入key
        //ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        //指定一个key，用这个取值或存值都不需要在输入key
        BoundValueOperations<String, String> ops = stringRedisTemplate.boundValueOps("hello");
        ops.set(UUID.fastUUID().toString());
        System.out.println("存入的UUID：" + ops.get());
    }
}
