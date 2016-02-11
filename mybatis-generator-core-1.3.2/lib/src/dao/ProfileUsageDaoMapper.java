package dao;

import model.ProfileUsageDao;

public interface ProfileUsageDaoMapper {
    int deleteByPrimaryKey(Integer offerId);

    int insert(ProfileUsageDao record);

    int insertSelective(ProfileUsageDao record);

    ProfileUsageDao selectByPrimaryKey(Integer offerId);

    int updateByPrimaryKeySelective(ProfileUsageDao record);

    int updateByPrimaryKey(ProfileUsageDao record);
}