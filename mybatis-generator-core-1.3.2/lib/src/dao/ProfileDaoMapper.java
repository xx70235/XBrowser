package dao;

import model.ProfileDao;

public interface ProfileDaoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProfileDao record);

    int insertSelective(ProfileDao record);

    ProfileDao selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProfileDao record);

    int updateByPrimaryKey(ProfileDao record);
}