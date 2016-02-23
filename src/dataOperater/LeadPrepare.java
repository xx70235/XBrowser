package dataOperater;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import com.gargoylesoftware.htmlunit.javascript.host.Proxy;

import dao.OfferDaoMapper;
import dao.ProfileDaoMapper;
import dao.ProfileUsageDaoMapper;
import dao.ProxyDaoMapper;
import dao.ProxyUsageDaoMapper;
import main.BrowserOperation;
import model.DbipLookupDao;
import model.OfferDao;
import model.ProfileDao;
import model.ProfileUsageDao;
import model.ProxyDao;
import model.ProxyUsageDao;
import utils.ProxyDiedException;

public class LeadPrepare {

//	OfferDao offer;
//	ProxyUsageDao proxyUsage;
//	ProxyDao proxy;
//	ProfileDao profile;
//	ProfileUsageDao profileUsage;

	private static SqlSessionFactory sqlSessionFactory;
	private static Reader reader;
	private final Logger logger = Logger.getLogger(LeadPrepare.class);
	static {
		try {
			reader = Resources.getResourceAsReader("Configuration.xml");
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LeadPrepare()
	{
		
	}
	
	/**
	 * 通过浏览器打开网页获得当前的代理（只有通过小猪代理才需要这样做），然后查表得到代理的州和城市
	 * @param browserOperation
	 * @return 返回代理对象
	 * @throws ProxyDiedException
	 * @throws UnknownHostException
	 */
	public ProxyDao prepareProxy(BrowserOperation browserOperation) throws ProxyDiedException, UnknownHostException
	{
		// 获得当前proxy的ip地址
		String proxyIp = browserOperation.getIp();

		// 根据当前的proxy的ip初始化proxyDao
		ProxyDao proxyDao = new ProxyDao();
		proxyDao.setIp(proxyIp);

		// 通过查询数据库得出proxy的所在州和城市
		// 通过ip得到InetAddress对象，ip库查询时需要
		InetAddress proxyInetAddress = InetAddress.getByName(proxyDao.getIp());

		// 查找InetAddress对象的地址
		DbipLookupDao dbipLookupDao = ProxyLookup.getAddressByIp(proxyInetAddress);

		// 将地址写入到ProxyDao对象
		proxyDao.setState(dbipLookupDao.getStateprov());
		proxyDao.setCity(dbipLookupDao.getCity());
		return proxyDao;
	}
	
//	public LeadPrepare(OfferDao offer, ProxyUsageDao proxyUsage, ProfileDao profile, ProfileUsageDao profileUsage) {
//		this.offer = offer;
//		this.proxyUsage = proxyUsage;
//		this.profile = profile;
//		this.profileUsage = profileUsage;
//	}

	/**
	 * 通过offerPhone和proxy的ip在proxyusage表中查找该ip是否做过该广告，用过返回true，没用过返回false
	 * @param offerid
	 * @param proxyIp
	 * @return
	 */
	private boolean isIpUsed(int offerid, String proxyIp) {
		// 通过offerPhone和proxy的ip在proxyusage表中查找该ip是否做过该广告，用过返回true，没用过返回false

		SqlSession session = sqlSessionFactory.openSession();
		try {
			ProxyUsageDaoMapper proxyUsageOperation = session.getMapper(ProxyUsageDaoMapper.class);
			List<ProxyUsageDao> proxyUsageList = proxyUsageOperation.selectByOfferIdAndProxyIp(offerid,proxyIp);
			for (ProxyUsageDao proxyUsage : proxyUsageList) {
				logger.info("当前代理已做过该广告: "+proxyUsage.getIp()+"---"+proxyUsage.getOfferId()+"---"+proxyUsage.getUseTime());
				
			}
			if (!proxyUsageList.isEmpty())
				return true;
			else {
				return false;
			}

		} finally {
			session.close();
		}
	}

	/**
	 * 根据当前offer的id，类别，当前代理的州和城市获取资料profile，分为两步：
	 * 1.首先查找db中是否有符合广告类型和代理地址的资料，并且该资料不能在该广告中用过（查profile_usage表），如果有，返回该资料
	 * 2.如果数据库中没有符合要求的资料，则从网站上取,将其页面所有的资料均放入数据库,取完之后放入profile数据库
	 * @param offerId 当前offer的id
	 * @param offerCategory 当前offer的类别
	 * @param proxyState 当前代理的州
	 * @param proxyCity 当前代理的城市
	 * @return
	 */
	private ProfileDao getProfileByOfferCategoryAndProxyAddr(int offerId, String offerCategory, String proxyState,
			String proxyCity) {
		// 首先查找db中是否有符合广告类型和代理地址的资料，并且该资料不能在该广告中用过（查profile_usage表），
		// 如果有，返回该资料
		SqlSession session = sqlSessionFactory.openSession();
		try {
			ProfileDaoMapper profileOperation = session.getMapper(ProfileDaoMapper.class);
			List<ProfileDao> profileList = profileOperation.selectByCategoryAndAddr(offerCategory, proxyState,
					proxyCity);

			for (ProfileDao profile : profileList) {
				if (!isfProfileUsed(profile.getId(), offerId)) {
					return profile;
				}
			}
		} finally {
			session.close();
		}

		
		ProfileDao profileDao = getProfileFromWeb(offerCategory, proxyState, proxyCity);
		
		return profileDao;
	}
	
	/**
	 * 如果数据库中没有符合要求的资料，则从网站上取,将其页面所有的资料均放入数据库
	 * 取完之后放入profile数据库
	 * @param offerCategory
	 * @param proxyState
	 * @param proxyCity
	 * @return
	 */
	private ProfileDao getProfileFromWeb(String offerCategory, String proxyState,
			String proxyCity)
	{
		
				WebProfileOperation webProfileOperation = new WebProfileOperation();
				ProfileDao profileDao = webProfileOperation.getProfileFromWeb(offerCategory, proxyState, proxyCity);
				return profileDao;
	}

	/**
	 * 判断当前的资料profile是否在当前offer中使用过（查询profileusage表）
	 * @param profileId
	 * @param offerId
	 * @return 
	 */
	private boolean isfProfileUsed(int profileId, int offerId) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			ProfileUsageDaoMapper profileUsageOperation = session.getMapper(ProfileUsageDaoMapper.class);
			List<ProfileUsageDao> profileUsageList = profileUsageOperation.selectByProfileIdAndOfferId(profileId,
					offerId);

			if (!profileUsageList.isEmpty()) {
				return true;
			} else {
				return false;
			}
		} finally {
			session.close();
		}
	}
	
	/**
	 * 通过网站返回参数得到当前的ip地址（适用于使用VPN或直连时）
	 * @return
	 */
	public String getIp()
	{
		URL myIP;
		 try 
	        {
	            myIP = new URL("http://myip.dnsomatic.com/");

	            BufferedReader in = new BufferedReader(
	                    new InputStreamReader(myIP.openStream())
	                    );
	            return in.readLine();
	        } catch (Exception e1) 
	        {
	            try {
	                myIP = new URL("http://icanhazip.com/");

	                BufferedReader in = new BufferedReader(
	                        new InputStreamReader(myIP.openStream())
	                        );
	                return in.readLine();
	            } catch (Exception e2) {
	                e2.printStackTrace(); 
	            }
	        }
		 return null;
	}

	/**
	 * 通过当前offer和当前代理的情况获取资料profile，分为两个步骤:
	 * 1.验证代理是否已在当前offer上使用过，如果使用过则返回null，
	 * 2.根据广告类型（category）和代理的州地址城市地址获取资料profile
	 * @param offer 当前offer
	 * @param proxy 当前代理
	 * @return
	 */
	public ProfileDao prepare(OfferDao offer, ProxyDao proxy) {
		
		// 验证代理是否用过
		if (isIpUsed(offer.getId(), proxy.getIp())) {
			logger.info("当前代理"+proxy.getIp()+"已做过广告"+offer.getName()+"。");
			return null;
		} 
		
		// 根据广告类型和代理地址取资料
		else {
			return getProfileByOfferCategoryAndProxyAddr(offer.getId(), offer.getCategory(), proxy.getState(),
					proxy.getCity());
		}

		
	}

	public static void main(String[] args) {
		
		OfferDao offer = new OfferDao();
		offer.setCategory("dating");
		offer.setId(1);
		offer.setUrl("www.click.com");
		
		ProxyDao proxy  =new ProxyDao();
		proxy.setId(1);
		proxy.setIp("0.0.0.1");
		proxy.setState("NY");
		proxy.setCity("new york");
		
		LeadPrepare leadPrepare  = new LeadPrepare();
		leadPrepare.prepare(offer, proxy);
		leadPrepare.getProfileFromWeb(offer.getCategory(), proxy.getState(), proxy.getCity());

	}
}