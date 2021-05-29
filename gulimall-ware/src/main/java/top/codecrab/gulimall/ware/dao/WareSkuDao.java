package top.codecrab.gulimall.ware.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.codecrab.gulimall.ware.entity.WareSkuEntity;

/**
 * 商品库存
 *
 * @author codecrab
 * @date 2021-05-28 22:50:44
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

}