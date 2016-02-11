package main;

import org.jboss.netty.handler.codec.spdy.SpdySettingsFrame;

import dataOperater.LeadPrepare;
import dataOperater.OfferOperation;

/**
 * 主测试类，模拟从获取广告到获取资料的过程
 * @author B203
 *
 */
public class MainTest {

	//获取广告操作
	OfferOperation offerOperation;
	//验证代理是否用过，并取合适的资料操作
	LeadPrepare leadPrepare;
	
	public MainTest()
	{
		offerOperation = new OfferOperation();
		leadPrepare = new LeadPrepare();
	}
	
	public static void main(String[] args)
	{
		MainTest mainTest = new MainTest();
		
	}
	
}
