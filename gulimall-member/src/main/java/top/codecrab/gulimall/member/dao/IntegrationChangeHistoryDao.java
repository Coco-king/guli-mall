package top.codecrab.gulimall.member.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.codecrab.gulimall.member.entity.IntegrationChangeHistoryEntity;

/**
 * 积分变化历史记录
 *
 * @author codecrab
 * @date 2021-05-28 22:40:42
 */
@Mapper
public interface IntegrationChangeHistoryDao extends BaseMapper<IntegrationChangeHistoryEntity> {

}
