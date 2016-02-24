package dataOperater;

import java.io.Reader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


import dao.DbipLookupDaoMapper;
import dao.OfferDaoMapper;
import model.DbipLookupDao;
import model.OfferDao;
import model.StateNameMap;

public class ProxyLookup {

	 private static SqlSessionFactory sqlSessionFactory;
	    private static Reader reader;

	    static{
	        try{
	        reader    = Resources.getResourceAsReader("Configuration.xml");
	        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
	    }catch(Exception e){
	        e.printStackTrace();
	    }
	    }
	
	InetAddress proxyIp;
	
	/**
	 * 通过ip地址查找得到dbipLookupDao对象，主要用于得到国家、州、城市
	 * @param proxyIp
	 * @return
	 */
	 public static DbipLookupDao getAddressByIp(InetAddress proxyIp)
	    {
	        SqlSession session = sqlSessionFactory.openSession();
	        try{
	            DbipLookupDaoMapper dbipLookupDaoMapper = session.getMapper(DbipLookupDaoMapper.class);
	            Map<String,byte[]> map = new HashMap<String,byte[]>();
	            map.put("ipStart", proxyIp.getAddress());
	            DbipLookupDao dbipLookupDao = dbipLookupDaoMapper.selectByPrimaryKey(map);
	            dbipLookupDao.setStateprov(StateNameMap.getStateShortName(dbipLookupDao.getStateprov().toLowerCase()));
	            return dbipLookupDao;

	        }
	        finally
	        {
	            session.close();
	        }
	    }
	 
	 public  static void  main(String[] args) {
		 
		 try {
			InetAddress proxyIp = InetAddress.getByName("210.72.27.55");
			ProxyLookup proxyLookup = new ProxyLookup();
			DbipLookupDao dbipLookupDao = proxyLookup.getAddressByIp(proxyIp);
			System.out.println(dbipLookupDao.getCountry()+" "+dbipLookupDao.getCity());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
