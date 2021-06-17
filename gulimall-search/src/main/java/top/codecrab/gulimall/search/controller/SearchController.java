package top.codecrab.gulimall.search.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import top.codecrab.gulimall.search.service.MallSearchService;
import top.codecrab.gulimall.search.vo.SearchParam;
import top.codecrab.gulimall.search.vo.SearchResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author codecrab
 * @since 2021年06月15日 17:00
 */
@Controller
public class SearchController {

    @Resource
    private MallSearchService mallSearchService;

    @GetMapping("/list.html")
    public String listPage(SearchParam param, Model model, HttpServletRequest request) {
        SearchResult result = mallSearchService.search(param, request.getQueryString());
        model.addAttribute("result", result);
        model.addAttribute("keyword", param.getKeyword());
        return "list";
    }

}
