package dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import model.ProxyDao;
import model.ProxyUsageDao;

public interface ProxyUsageDaoMapper {
	List<ProxyUsageDao> selectByOfferIdAndProxyIp(@Param("offer_id")int offerId, @Param("ip")String proxyIp);
	
    int insert(ProxyUsageDao record);

    int insertSelective(ProxyUsageDao record);
}