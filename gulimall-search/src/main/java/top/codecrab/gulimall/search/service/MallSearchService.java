package top.codecrab.gulimall.search.service;

import top.codecrab.gulimall.search.vo.SearchParam;
import top.codecrab.gulimall.search.vo.SearchResult;

/**
 * @author codecrab
 * @since 2021年06月15日 17:31
 */
public interface MallSearchService {

    /**
     * 检索商品
     *
     * @param param 检索参数
     * @return 检索结果
     */
    SearchResult search(SearchParam param);
}
