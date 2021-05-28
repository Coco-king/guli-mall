package top.codecrab.gulimall.member.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.codecrab.gulimall.member.entity.MemberStatisticsInfoEntity;

/**
 * 会员统计信息
 *
 * @author codecrab
 * @date 2021-05-28 22:40:42
 */
@Mapper
public interface MemberStatisticsInfoDao extends BaseMapper<MemberStatisticsInfoEntity> {

}
