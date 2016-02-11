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
		// ͨ��offerPhone��proxy��ip��proxyusage���в��Ҹ�ip�Ƿ������ù�棬�ù�����true��û�ù�����false

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
		// ���Ȳ���db���Ƿ��з��Ϲ�����ͺʹ����ַ�����ϣ����Ҹ����ϲ����ڸù�����ù�����profile_usage����
		// ����У����ظ�����
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

		
		// ������ݿ���û�з���Ҫ������ϣ������վ��ȡ,����ҳ�����е����Ͼ��������ݿ�,�������Ĵ���ʵ�֡�
		// ȡ��֮�����profile���ݿ�
		
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
		// ��֤�����Ƿ��ù�

		if (isIpUsed(offer.getId(), proxy.getIp())) {
			System.out.println("��ǰ����"+proxy.getIp()+"���������"+offer.getName()+"��");
			return null;
		} else {
			return getProfileByOfferCategoryAndProxyAddr(offer.getId(), offer.getCategory(), proxy.getState(),
					proxy.getCity());
		}

		// ���ݹ�����ͺʹ����ַȡ����
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