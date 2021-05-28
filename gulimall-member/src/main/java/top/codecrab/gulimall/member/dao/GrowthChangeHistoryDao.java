package top.codecrab.gulimall.member.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.codecrab.gulimall.member.entity.GrowthChangeHistoryEntity;

/**
 * 成长值变化历史记录
 *
 * @author codecrab
 * @date 2021-05-28 22:40:42
 */
@Mapper
public interface GrowthChangeHistoryDao extends BaseMapper<GrowthChangeHistoryEntity> {

}
