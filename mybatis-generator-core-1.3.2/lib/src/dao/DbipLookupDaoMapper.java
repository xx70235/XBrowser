package dao;

import model.DbipLookupDao;

public interface DbipLookupDaoMapper {
    int deleteByPrimaryKey(byte[] ipStart);

    int insert(DbipLookupDao record);

    int insertSelective(DbipLookupDao record);

    DbipLookupDao selectByPrimaryKey(byte[] ipStart);

    int updateByPrimaryKeySelective(DbipLookupDao record);

    int updateByPrimaryKeyWithBLOBs(DbipLookupDao record);

    int updateByPrimaryKey(DbipLookupDao record);
}