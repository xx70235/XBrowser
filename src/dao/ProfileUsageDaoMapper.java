package dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import model.ProfileUsageDao;

public interface ProfileUsageDaoMapper {
    int deleteByPrimaryKey(Integer offerId);

    int insert(ProfileUsageDao record);

    int insertSelective(ProfileUsageDao record);

    ProfileUsageDao selectByPrimaryKey(Integer offerId);
    
    List<ProfileUsageDao> selectByProfileIdAndOfferId(@Param("profileId")int profileId,@Param("offerId")int offerId);

    int updateByPrimaryKeySelective(ProfileUsageDao record);

    int updateByPrimaryKey(ProfileUsageDao record);
}