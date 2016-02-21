package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.functors.WhileClosure;
import org.apache.james.mime4j.storage.TempFileStorageProvider;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;


import model.OfferDao;
import model.ProfileDao;
import model.ProxyDao;
import utils.ProxyDiedException;

public class BrowserOperation {

	private WebDriver driver;
	private FirefoxProfile profile;
	private static String baseUrl = "null";
	private static String targetUrl = "null";
	ArrayList<String> alList;
	ArrayList<String> agentList;
	private final Logger logger = Logger.getLogger(BrowserOperation.class);
	/**
	 * ��ʼ���������������ȡ����������profiles���������ԡ�����useragent��
	 */
	private void initial() {
		File file = new File("profiles");
		profile = new FirefoxProfile(file);
		// setProxy();
		generateAcceptLangList("acceptLanguage.txt");
		generateAgent("useragent.txt");
		setAcceptLang();
		setAgent();
	}

	/**
	 * �������
	 * 
	 * @throws Exception
	 */
	public void openBrowser() throws Exception {
		showCurrentTime("��ʼ��ʼ��");
		initial();
		setProxy();
		showCurrentTime("��ʼ�������");
		driver = new FirefoxDriver(profile);

		// baseUrl = "null";
		// driver.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);
	}

	public void openBrowser(ProxyDao proxyDao) throws Exception {
		showCurrentTime("��ʼ��ʼ��");
		initial();
		setProxy(proxyDao);
		showCurrentTime("��ʼ�������");
		driver = new FirefoxDriver(profile);

	}

	public void generateAcceptLangList(String filepath) {
		alList = new ArrayList<String>();
		BufferedReader bReader;
		try {
			bReader = new BufferedReader(new FileReader(new File("AcceptLanguage.txt")));
			String tmp = "";
			while ((tmp = bReader.readLine()) != null) {
				alList.add(tmp);
			}

			bReader.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}

	}

	public void generateAgent(String filepath) {
		agentList = new ArrayList<String>();
		BufferedReader bReader;
		try {
			bReader = new BufferedReader(new FileReader(new File("useragent.txt")));
			String tmp = "";
			while ((tmp = bReader.readLine()) != null) {
				agentList.add(tmp);
			}

			bReader.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}

	}

	public void setAgent() {
		Random random = new Random();
		int i = random.nextInt(agentList.size());
		String agent = agentList.get(i);
		if (profile != null) {
			profile.setPreference("general.useragent.override", agent);

		}
	}

	public void setAcceptLang() {
		Random random = new Random();
		int i = random.nextInt(alList.size());
		String acceptLang = alList.get(i);
		if (profile != null) {
			profile.setPreference("Accept-Language", acceptLang);

		}
	}

	/**
	 * ���õ�ǰ���������Ĵ���ΪС���������еĴ���
	 */
	private void setProxy() {
		profile.setPreference("network.proxy.type", 1);
		profile.setPreference("network.proxy.socks", "127.0.0.1");
		profile.setPreference("network.proxy.socks_port", 9951);
	}

	/**
	 * ���õ�ǰ������������
	 * 
	 * @param proxyDao
	 */
	private void setProxy(ProxyDao proxyDao) {
		profile.setPreference("network.proxy.type", 1);
		profile.setPreference("network.proxy.socks", proxyDao.getIp());
		profile.setPreference("network.proxy.socks_port", proxyDao.getPort());
	}

	/**
	 * ɾ��������е�cookies
	 */
	public void deleteAllCookies() {
		driver.manage().deleteAllCookies();
	}

	/**
	 * ��url�����150����δ���������ж��̣߳�������false
	 * 
	 * @param url
	 */
	public boolean openUrl(final String url) {
		// �жϴ�ҳ���Ƿ�ɹ�
		boolean isOpenSuccess = true;
		int openTime = 0;
		do {
			Thread t = new Thread(new Runnable() {
				public void run() {
					showCurrentTime("��ʼ����ҳ");
					driver.get(Thread.currentThread().getName());
				}
			}, url);
			t.start();
			try {
				t.join(150000);
			} catch (InterruptedException e) { // ignore
			}
			if (t.isAlive()) { // Thread still alive, we need to abort
				// logger.warning("Timeout on loading page " + url);
				showCurrentTime("����ҳ��ҳ��ʱ");
				// System.out.println("timeout on loading page");
				t.interrupt();
				showCurrentTime("���interrupt");
				return false;
			}
			showCurrentTime("����ҳ���");
			String tmp = driver.findElement(By.tagName("body")).getText();
			if (tmp.contains("Unable to connect")) {
				isOpenSuccess = false;
				openTime++;
			}
		} while (!isOpenSuccess&&openTime<3);

		return true;
	}

	/**
	 * ��ʾ��ǰ���ں�ʱ�䣬����ʾ����ע��
	 */
	private void showCurrentTime(String text) {
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String TimeString = time.format(new java.util.Date());
		//System.out.println(TimeString + "--" + text);
		logger.info(TimeString + "--" + text);
	}

	/**
	 * �ر������
	 */
	public void closeBrowser() {
		showCurrentTime("�ر������");
		driver.quit();
		showCurrentTime("��ɹر������");
	}

	/**
	 * ִ�й�棺���ȴ��������Ȼ�������Ӧ�Ĺ��ִ��class�����ر�������� ������������ʱ����ʾ�������ˣ���������
	 * 
	 * @param offerDao
	 * @param profileDao
	 * @return
	 */
	public boolean executeOffer(OfferDao offerDao, ProfileDao profileDao) {
		try {
			boolean executionSuccess = true;
			this.openBrowser();
			// ����Ҫ����httpЭ���httpsЭ�飬������׳�����http://stackoverflow.com/questions/22145776/selenium-org-openqa-selenium-webdriverexception-f-queryinterface-is-not-a-fun
			executionSuccess = this.openUrl(offerDao.getUrl());
			if (!executionSuccess) {
				logger.info("����ʧЧ������������������г���");
				this.closeBrowser();
				return false;
			} else {
				this.closeBrowser();
				return true;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ͨ����վ���ز����õ���ǰ��ip��ַ��������ʹ�ó������ô����������С�����
	 * 
	 * @return
	 */
	public String getIp() throws ProxyDiedException{
		try {
			boolean executionSuccess = true;
			this.openBrowser();
			// ����Ҫ����httpЭ���httpsЭ�飬������׳�����http://stackoverflow.com/questions/22145776/selenium-org-openqa-selenium-webdriverexception-f-queryinterface-is-not-a-fun
			executionSuccess = this.openUrl("http://myip.dnsomatic.com/");
			String myIP = driver.findElement(By.tagName("body")).getText();
			if (!executionSuccess) {
				//����ʧЧ������������������г���
				this.closeBrowser();
				throw new ProxyDiedException();
//				return null;
			} else {
				this.closeBrowser();
				return myIP;
			}

		} catch (NoSuchElementException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		BrowserOperation browserOperation = new BrowserOperation();
		try {
			for (int i = 0; i < 100; i++) {
				browserOperation.openBrowser();
				// ����Ҫ����httpЭ���httpsЭ�飬������׳�����http://stackoverflow.com/questions/22145776/selenium-org-openqa-selenium-webdriverexception-f-queryinterface-is-not-a-fun
				browserOperation.openUrl("http://www.check2ip.com");
				browserOperation.closeBrowser();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
