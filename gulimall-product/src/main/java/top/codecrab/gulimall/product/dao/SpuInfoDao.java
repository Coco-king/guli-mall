package top.codecrab.gulimall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.codecrab.gulimall.product.entity.SpuInfoEntity;

/**
 * spu信息
 *
 * @author codecrab
 * @date 2021-05-28 22:19:48
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {

    void updateStatus(@Param("spuId") Long spuId, @Param("code") int code);
}
