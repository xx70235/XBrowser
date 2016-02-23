package main;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.jboss.netty.handler.codec.spdy.SpdySettingsFrame;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.openqa.selenium.NoSuchElementException;

import com.google.common.net.InetAddresses;

import bsh.This;
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
	private OperateLead operateLead;
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

	public OperateLead getOperateLead() {
		return operateLead;
	}

	public void setOperateLead(OperateLead operateLead) {
		this.operateLead = operateLead;
	}

	public MainTest() {
		offerOperation = new OfferOperation();
		leadPrepare = new LeadPrepare();
		operateLead = new OperateLead();
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
	public static void main(String[] args) {
		MainTest mainTest = new MainTest();

		@SuppressWarnings("static-access")
		List<OfferDao> offerList = mainTest.getOfferOperation().getAllOffers();
		LeadPrepare leadPrepare = mainTest.getLeadPrepare();
		OperateLead operateLead = mainTest.getOperateLead();
		BrowserOperation browserOperation = null;
		
		// 将列表中的广告顺序随机打乱
		Collections.shuffle(offerList);

		// 浏览器执行（打开网页、执行广告是否成功）
		try {
			for (OfferDao offerDao : offerList) {
				// 获取代理信息
				browserOperation = new BrowserOperation();
				ProxyDao proxyDao = leadPrepare.prepareProxy(browserOperation);
				browserOperation.closeBrowser();

				// 获取资料
				ProfileDao profileDao = leadPrepare.prepare(offerDao, proxyDao);

				// 开始执行广告
				browserOperation = new BrowserOperation();
				operateLead.setBrowserOperation(browserOperation);
				operateLead.execute(offerDao, profileDao);
			}
		} catch (UnknownHostException e) {
			logger.error(e.getLocalizedMessage() + e.getMessage());
			if (browserOperation != null) {
				browserOperation.closeBrowser();
			}
		} catch (ProxyDiedException e) {
			logger.info("当前代理失效，请更换代理");
			if (browserOperation != null) {
				browserOperation.closeBrowser();
			}
		} catch (NoSuchElementException e) {
			logger.info("当前代理失效，请更换代理");
			if (browserOperation != null) {
				browserOperation.closeBrowser();
			}
		} catch (Exception e2) {
			logger.error(e2.getMessage());
			if (browserOperation != null) {
				browserOperation.closeBrowser();
			}
		}

	}

}
