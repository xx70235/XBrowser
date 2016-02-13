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
 * �������࣬ģ��ӻ�ȡ��浽��ȡ���ϵĹ���
 * @author B203
 *
 */
public class MainTest {

	//��ȡ������
	private OfferOperation offerOperation;
	//��֤�����Ƿ��ù�����ȡ���ʵ����ϲ���
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
		//proxy�б�ѭ����һ����ǰProxy���ж�Ϊdie��continue
		for(ProxyDao proxyDao: proxyList)
		{
			InetAddress proxyIp;
			try {
				//ͨ����ѯ���ݿ�ó�proxy�������ݺͳ���
				//ͨ��ip�õ�InetAddress����
				proxyIp = InetAddress.getByName(proxyDao.getIp());
				//����InetAddress����ĵ�ַ
				DbipLookupDao dbipLookupDao = ProxyLookup.getAddressByIp(proxyIp);
				//����ַд�뵽ProxyDao����
				proxyDao.setState(dbipLookupDao.getStateprov());
				proxyDao.setCity(dbipLookupDao.getCity());
				
				//Offerѭ��
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
