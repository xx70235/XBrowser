package main;

import java.util.List;

import org.jboss.netty.handler.codec.spdy.SpdySettingsFrame;

import dataOperater.LeadPrepare;
import dataOperater.OfferOperation;
import model.OfferDao;

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
		for(OfferDao offerDao:offerList)
		{
			
//			leadPrepare.
		}
	}
	
}
