package dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import model.DbipLookupDao;

public interface DbipLookupDaoMapper {
    int deleteByPrimaryKey(byte[] ipStart);

    int insert(DbipLookupDao record);

    int insertSelective(DbipLookupDao record);

    DbipLookupDao selectByPrimaryKey(Map<String,byte[]> map);
    
    //List<DbipLookupDao> selectByIp(byte[] ip);

    int updateByPrimaryKeySelective(DbipLookupDao record);

    int updateByPrimaryKeyWithBLOBs(DbipLookupDao record);

    int updateByPrimaryKey(DbipLookupDao record);
}