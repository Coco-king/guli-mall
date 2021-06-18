package top.codecrab.gulimall.product.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.codecrab.gulimall.product.service.SkuInfoService;
import top.codecrab.gulimall.product.web.vo.sku.SkuItemVo;

import javax.annotation.Resource;

/**
 * @author codecrab
 * @since 2021年06月18日 15:41
 */
@Controller
public class ItemController {

    @Resource
    private SkuInfoService skuInfoService;

    @GetMapping("/{skuId}.html")
    public String item(@PathVariable Long skuId, Model model) {
        SkuItemVo skuItemVo = skuInfoService.item(skuId);
        model.addAttribute("item", skuItemVo);

        return "item";
    }
}
