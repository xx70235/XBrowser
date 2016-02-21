package main;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jboss.netty.handler.codec.spdy.SpdySettingsFrame;

import com.google.common.net.InetAddresses;

import dataOperater.LeadPrepare;
import dataOperater.OfferOperation;
import dataOperater.ProxyLookup;
import model.DbipLookupDao;
import model.OfferDao;
import model.ProfileDao;
import model.ProxyDao;
import utils.ProxyDiedException;

/**
 * 主测试类，模拟从获取广告到获取资料的过程
 * 
 * @author B203
 *
 */
public class MainTest {

	// 获取广告操作
	private OfferOperation offerOperation;
	// 验证代理是否用过，并取合适的资料操作
	private LeadPrepare leadPrepare;
	private final static Logger logger = Logger.getLogger(MainTest.class);
	public OfferOperation getOfferOperation() {
		return offerOperation;
	}

	public void setOfferOperation(OfferOperation offerOperation) {
		this.offerOperation = offerOperation;
	}

	public LeadPrepare getLeadPrepare() {
		return leadPrepare;
	}

	public void setLeadPrepare(LeadPrepare leadPrepare) {
		this.leadPrepare = leadPrepare;
	}

	public MainTest() {
		offerOperation = new OfferOperation();
		leadPrepare = new LeadPrepare();
	}

	// public static void main(String[] args)
	// {
	// MainTest mainTest = new MainTest();
	// List<OfferDao> offerList = mainTest.getOfferOperation().getAllOffers();
	// LeadPrepare leadPrepare = mainTest.getLeadPrepare();
	//
	// List<ProxyDao> proxyList = new ArrayList<ProxyDao>();
	// //proxy列表循环，一旦当前Proxy被判定为die则continue
	// for(ProxyDao proxyDao: proxyList)
	// {
	// InetAddress proxyIp;
	// try {
	// //通过查询数据库得出proxy的所在州和城市
	// //通过ip得到InetAddress对象
	// proxyIp = InetAddress.getByName(proxyDao.getIp());
	// //查找InetAddress对象的地址
	// DbipLookupDao dbipLookupDao = ProxyLookup.getAddressByIp(proxyIp);
	// //将地址写入到ProxyDao对象
	// proxyDao.setState(dbipLookupDao.getStateprov());
	// proxyDao.setCity(dbipLookupDao.getCity());
	//
	// //Offer循环
	// for(OfferDao offerDao:offerList)
	// {
	// ProfileDao profileDao= leadPrepare.prepare(offerDao, proxyDao);
	// }
	//
	// } catch (UnknownHostException e) {
	// e.printStackTrace();
	// }
	//
	//
	// }
	//
	// }

	/**
	 * 用于测试vip72代理情况下（单条代理用程序加载）时使用。
	 * 
	 * @param args
	 */
	public static void main(String[] args)  {
		MainTest mainTest = new MainTest();
		BrowserOperation browserOperation = new BrowserOperation();
		@SuppressWarnings("static-access")
		List<OfferDao> offerList = mainTest.getOfferOperation().getAllOffers();
		LeadPrepare leadPrepare = mainTest.getLeadPrepare();

		// 浏览器执行（打开网页、执行广告是否成功）
//		boolean executionSuccess = true;
		for (OfferDao offerDao : offerList) {
			try {
				//获得当前proxy的ip地址
				String proxyIp = browserOperation.getIp();
				
				//根据当前的proxy的ip初始化proxyDao
				ProxyDao proxyDao = new ProxyDao();
				proxyDao.setIp(proxyIp);
				
				//通过查询数据库得出proxy的所在州和城市
				//通过ip得到InetAddress对象，ip库查询时需要
				InetAddress proxyInetAddress = InetAddress.getByName(proxyDao.getIp());
				
				//查找InetAddress对象的地址
				DbipLookupDao dbipLookupDao = ProxyLookup.getAddressByIp(proxyInetAddress);
				
				//将地址写入到ProxyDao对象
				proxyDao.setState(dbipLookupDao.getStateprov());
				proxyDao.setCity(dbipLookupDao.getCity());
				ProfileDao profileDao = leadPrepare.prepare(offerDao, proxyDao);
				
				//开始执行广告
				browserOperation.executeOffer(offerDao, profileDao);
				
				
			} catch (UnknownHostException e) {
				logger.error(e.getLocalizedMessage()+e.getMessage());
			} catch(ProxyDiedException e)
			{
				logger.info("当前代理失效，请更换代理");
				if(browserOperation !=null)
				{
					browserOperation.closeBrowser();
				}
			}
			catch (Exception e2) {
				logger.error(e2.getMessage());
			}

		}
	}
}
