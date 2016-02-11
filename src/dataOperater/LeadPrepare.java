package dataOperater;

import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.gargoylesoftware.htmlunit.javascript.host.Proxy;

import dao.OfferDaoMapper;
import dao.ProfileDaoMapper;
import dao.ProfileUsageDaoMapper;
import dao.ProxyDaoMapper;
import dao.ProxyUsageDaoMapper;
import model.OfferDao;
import model.ProfileDao;
import model.ProfileUsageDao;
import model.ProxyDao;
import model.ProxyUsageDao;

public class LeadPrepare {

//	OfferDao offer;
//	ProxyUsageDao proxyUsage;
//	ProxyDao proxy;
//	ProfileDao profile;
//	ProfileUsageDao profileUsage;

	private static SqlSessionFactory sqlSessionFactory;
	private static Reader reader;

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
	
//	public LeadPrepare(OfferDao offer, ProxyUsageDao proxyUsage, ProfileDao profile, ProfileUsageDao profileUsage) {
//		this.offer = offer;
//		this.proxyUsage = proxyUsage;
//		this.profile = profile;
//		this.profileUsage = profileUsage;
//	}

	private boolean isIpUsed(int offerid, String proxyIp) {
		// 通过offerPhone和proxy的ip在proxyusage表中查找该ip是否做过该广告，用过返回true，没用过返回false

		SqlSession session = sqlSessionFactory.openSession();
		try {
			ProxyUsageDaoMapper proxyUsageOperation = session.getMapper(ProxyUsageDaoMapper.class);
			List<ProxyUsageDao> proxyUsageList = proxyUsageOperation.selectByOfferIdAndProxyIp(offerid,proxyIp);
			for (ProxyUsageDao proxyUsage : proxyUsageList) {
				System.out.println(proxyUsage.getIp());
				System.out.println(proxyUsage.getOfferId().toString());
				System.out.println(proxyUsage.getUseTime().toString());
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

		
		// 如果数据库中没有符合要求的资料，则从网站上取,将其页面所有的资料均放入数据库,根据许冬的代码实现。
		// 取完之后放入profile数据库
		
		WebProfileOperation webProfileOperation = new WebProfileOperation();
		ProfileDao profileDao = webProfileOperation.getProfileFromWeb(offerCategory, proxyState, proxyCity);
		
		return profileDao;
	}

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

	public ProfileDao prepare(OfferDao offer, ProxyDao proxy) {
		// 验证代理是否用过

		if (isIpUsed(offer.getId(), proxy.getIp())) {
			System.out.println("当前代理"+proxy.getIp()+"已做过广告"+offer.getName()+"。");
			return null;
		} else {
			return getProfileByOfferCategoryAndProxyAddr(offer.getId(), offer.getCategory(), proxy.getState(),
					proxy.getCity());
		}

		// 根据广告类型和代理地址取资料
	}

	public static void main(String[] args) {
		
		OfferDao offer = new OfferDao();
		offer.setCategory("insurance");
		offer.setId(1);
		offer.setUrl("www.click.com");
		
		ProxyDao proxy  =new ProxyDao();
		proxy.setId(1);
		proxy.setIp("0.0.0.1");
		proxy.setState("NY");
		proxy.setCity("new york");
		
		LeadPrepare leadPrepare  = new LeadPrepare();
		leadPrepare.prepare(offer, proxy);

	}
}