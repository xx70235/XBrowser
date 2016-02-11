package dao;

import model.ProxyDao;

public interface ProxyDaoMapper {
    int insert(ProxyDao record);

    int insertSelective(ProxyDao record);
}