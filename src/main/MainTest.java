package main;

import org.jboss.netty.handler.codec.spdy.SpdySettingsFrame;

import dataOperater.LeadPrepare;
import dataOperater.OfferOperation;

/**
 * �������࣬ģ��ӻ�ȡ��浽��ȡ���ϵĹ���
 * @author B203
 *
 */
public class MainTest {

	//��ȡ������
	OfferOperation offerOperation;
	//��֤�����Ƿ��ù�����ȡ���ʵ����ϲ���
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
