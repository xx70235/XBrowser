package dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;

import com.beust.jcommander.Parameter;

import model.ProfileDao;

public interface ProfileDaoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProfileDao record);

    int insertSelective(ProfileDao record);

    ProfileDao selectByPrimaryKey(Integer id);
    
    List<ProfileDao> selectByCategoryAndAddr(@Param("category")String category,@Param("state")String state, @Param("city")String city);

    int updateByPrimaryKeySelective(ProfileDao record);

    int updateByPrimaryKey(ProfileDao record);
}