package main;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.handler.codec.spdy.SpdySettingsFrame;

import com.google.common.net.InetAddresses;

import dataOperater.LeadPrepare;
import dataOperater.OfferOperation;
import dataOperater.ProxyLookup;
import model.DbipLookupDao;
import model.OfferDao;
import model.ProfileDao;
import model.ProxyDao;

/**
 * 主测试类，模拟从获取广告到获取资料的过程
 * @author B203
 *
 */
public class MainTest {

	//获取广告操作
	private OfferOperation offerOperation;
	//验证代理是否用过，并取合适的资料操作
	private LeadPrepare leadPrepare;
	
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

	public MainTest()
	{
		offerOperation = new OfferOperation();
		leadPrepare = new LeadPrepare();
	}
	
	public static void main(String[] args)
	{
		MainTest mainTest = new MainTest();
		List<OfferDao> offerList = mainTest.getOfferOperation().getAllOffers();
		LeadPrepare leadPrepare = mainTest.getLeadPrepare();
		
		List<ProxyDao> proxyList = new ArrayList<ProxyDao>();
		//proxy列表循环，一旦当前Proxy被判定为die则continue
		for(ProxyDao proxyDao: proxyList)
		{
			InetAddress proxyIp;
			try {
				//通过查询数据库得出proxy的所在州和城市
				//通过ip得到InetAddress对象
				proxyIp = InetAddress.getByName(proxyDao.getIp());
				//查找InetAddress对象的地址
				DbipLookupDao dbipLookupDao = ProxyLookup.getAddressByIp(proxyIp);
				//将地址写入到ProxyDao对象
				proxyDao.setState(dbipLookupDao.getStateprov());
				proxyDao.setCity(dbipLookupDao.getCity());
				
				//Offer循环
				for(OfferDao offerDao:offerList)
				{
					ProfileDao profileDao= leadPrepare.prepare(offerDao, proxyDao);
				}
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			
			
		}
		
	}
	
}
