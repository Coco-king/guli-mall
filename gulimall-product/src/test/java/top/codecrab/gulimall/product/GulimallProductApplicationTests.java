package top.codecrab.gulimall.product;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.codecrab.gulimall.product.entity.BrandEntity;
import top.codecrab.gulimall.product.service.BrandService;

import javax.annotation.Resource;

@SpringBootTest
class GulimallProductApplicationTests {

    @Resource
    private BrandService brandService;

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

}
