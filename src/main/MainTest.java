package main;

import java.util.List;

import org.jboss.netty.handler.codec.spdy.SpdySettingsFrame;

import dataOperater.LeadPrepare;
import dataOperater.OfferOperation;
import model.OfferDao;

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
		for(OfferDao offerDao:offerList)
		{
			
//			leadPrepare.
		}
	}
	
}
