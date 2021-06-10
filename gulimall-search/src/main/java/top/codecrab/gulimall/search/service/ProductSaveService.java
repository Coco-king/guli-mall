package top.codecrab.gulimall.search.service;

import top.codecrab.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author codecrab
 * @since 2021年06月10日 20:15
 */
public interface ProductSaveService {

    boolean produceStatusUp(List<SkuEsModel> modelList) throws IOException;

}
