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
	 * ͨ�����������ҳ��õ�ǰ�Ĵ���ֻ��ͨ��С��������Ҫ����������Ȼ����õ�������ݺͳ���
	 * @param browserOperation
	 * @return ���ش������
	 * @throws ProxyDiedException
	 * @throws UnknownHostException
	 */
	public ProxyDao prepareProxy(BrowserOperation browserOperation) throws ProxyDiedException, UnknownHostException
	{
		// ��õ�ǰproxy��ip��ַ
		String proxyIp = browserOperation.getIp();

		// ���ݵ�ǰ��proxy��ip��ʼ��proxyDao
		ProxyDao proxyDao = new ProxyDao();
		proxyDao.setIp(proxyIp);

		// ͨ����ѯ���ݿ�ó�proxy�������ݺͳ���
		// ͨ��ip�õ�InetAddress����ip���ѯʱ��Ҫ
		InetAddress proxyInetAddress = InetAddress.getByName(proxyDao.getIp());

		// ����InetAddress����ĵ�ַ
		DbipLookupDao dbipLookupDao = ProxyLookup.getAddressByIp(proxyInetAddress);

		// ����ַд�뵽ProxyDao����
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
	 * ͨ��offerPhone��proxy��ip��proxyusage���в��Ҹ�ip�Ƿ������ù�棬�ù�����true��û�ù�����false
	 * @param offerid
	 * @param proxyIp
	 * @return
	 */
	private boolean isIpUsed(int offerid, String proxyIp) {
		// ͨ��offerPhone��proxy��ip��proxyusage���в��Ҹ�ip�Ƿ������ù�棬�ù�����true��û�ù�����false

		SqlSession session = sqlSessionFactory.openSession();
		try {
			ProxyUsageDaoMapper proxyUsageOperation = session.getMapper(ProxyUsageDaoMapper.class);
			List<ProxyUsageDao> proxyUsageList = proxyUsageOperation.selectByOfferIdAndProxyIp(offerid,proxyIp);
			for (ProxyUsageDao proxyUsage : proxyUsageList) {
				logger.info("��ǰ�����������ù��: "+proxyUsage.getIp()+"---"+proxyUsage.getOfferId()+"---"+proxyUsage.getUseTime());
				
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
	 * ���ݵ�ǰoffer��id����𣬵�ǰ������ݺͳ��л�ȡ����profile����Ϊ������
	 * 1.���Ȳ���db���Ƿ��з��Ϲ�����ͺʹ����ַ�����ϣ����Ҹ����ϲ����ڸù�����ù�����profile_usage��������У����ظ�����
	 * 2.������ݿ���û�з���Ҫ������ϣ������վ��ȡ,����ҳ�����е����Ͼ��������ݿ�,ȡ��֮�����profile���ݿ�
	 * @param offerId ��ǰoffer��id
	 * @param offerCategory ��ǰoffer�����
	 * @param proxyState ��ǰ�������
	 * @param proxyCity ��ǰ����ĳ���
	 * @return
	 */
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

		
		ProfileDao profileDao = getProfileFromWeb(offerCategory, proxyState, proxyCity);
		
		return profileDao;
	}
	
	/**
	 * ������ݿ���û�з���Ҫ������ϣ������վ��ȡ,����ҳ�����е����Ͼ��������ݿ�
	 * ȡ��֮�����profile���ݿ�
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
	 * �жϵ�ǰ������profile�Ƿ��ڵ�ǰoffer��ʹ�ù�����ѯprofileusage��
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
	 * ͨ����վ���ز����õ���ǰ��ip��ַ��������ʹ��VPN��ֱ��ʱ��
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
	 * ͨ����ǰoffer�͵�ǰ����������ȡ����profile����Ϊ��������:
	 * 1.��֤�����Ƿ����ڵ�ǰoffer��ʹ�ù������ʹ�ù��򷵻�null��
	 * 2.���ݹ�����ͣ�category���ʹ�����ݵ�ַ���е�ַ��ȡ����profile
	 * @param offer ��ǰoffer
	 * @param proxy ��ǰ����
	 * @return
	 */
	public ProfileDao prepare(OfferDao offer, ProxyDao proxy) {
		
		// ��֤�����Ƿ��ù�
		if (isIpUsed(offer.getId(), proxy.getIp())) {
			logger.info("��ǰ����"+proxy.getIp()+"���������"+offer.getName()+"��");
			return null;
		} 
		
		// ���ݹ�����ͺʹ����ַȡ����
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