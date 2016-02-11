package dao;

import model.ProxyUsageDao;

public interface ProxyUsageDaoMapper {
    int insert(ProxyUsageDao record);

    int insertSelective(ProxyUsageDao record);
}