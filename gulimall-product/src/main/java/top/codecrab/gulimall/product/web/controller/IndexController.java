package top.codecrab.gulimall.product.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.codecrab.gulimall.product.entity.CategoryEntity;
import top.codecrab.gulimall.product.service.CategoryService;
import top.codecrab.gulimall.product.web.vo.Catalog2Vo;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author codecrab
 * @since 2021年06月11日 15:48
 */
@Controller
public class IndexController {

    @Resource
    private CategoryService categoryService;

    @GetMapping({"/", "index.html"})
    public String indexPage(Model model) {
        List<CategoryEntity> categories = categoryService.getLevel1Categories();
        model.addAttribute("categories", categories);
        return "index";
    }

    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String, List<Catalog2Vo>> catalogJson() {
        return categoryService.getCatalogJson();
    }

}
