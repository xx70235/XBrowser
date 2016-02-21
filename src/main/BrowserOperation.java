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
	 * 初始化浏览器，包括读取火狐浏览器的profiles、设置语言、设置useragent。
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
	 * 打开浏览器
	 * 
	 * @throws Exception
	 */
	public void openBrowser() throws Exception {
		showCurrentTime("开始初始化");
		initial();
		setProxy();
		showCurrentTime("开始打开浏览器");
		driver = new FirefoxDriver(profile);

		// baseUrl = "null";
		// driver.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);
	}

	public void openBrowser(ProxyDao proxyDao) throws Exception {
		showCurrentTime("开始初始化");
		initial();
		setProxy(proxyDao);
		showCurrentTime("开始打开浏览器");
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
	 * 设置当前火狐浏览器的代理为小猪代理程序中的代理
	 */
	private void setProxy() {
		profile.setPreference("network.proxy.type", 1);
		profile.setPreference("network.proxy.socks", "127.0.0.1");
		profile.setPreference("network.proxy.socks_port", 9951);
	}

	/**
	 * 设置当前火狐浏览器代理
	 * 
	 * @param proxyDao
	 */
	private void setProxy(ProxyDao proxyDao) {
		profile.setPreference("network.proxy.type", 1);
		profile.setPreference("network.proxy.socks", proxyDao.getIp());
		profile.setPreference("network.proxy.socks_port", proxyDao.getPort());
	}

	/**
	 * 删除浏览器中的cookies
	 */
	public void deleteAllCookies() {
		driver.manage().deleteAllCookies();
	}

	/**
	 * 打开url，如果150秒内未代开，则中断线程，并返回false
	 * 
	 * @param url
	 */
	public boolean openUrl(final String url) {
		// 判断打开页面是否成功
		boolean isOpenSuccess = true;
		int openTime = 0;
		do {
			Thread t = new Thread(new Runnable() {
				public void run() {
					showCurrentTime("开始打开网页");
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
				showCurrentTime("打开网页网页超时");
				// System.out.println("timeout on loading page");
				t.interrupt();
				showCurrentTime("完成interrupt");
				return false;
			}
			showCurrentTime("打开网页完毕");
			String tmp = driver.findElement(By.tagName("body")).getText();
			if (tmp.contains("Unable to connect")) {
				isOpenSuccess = false;
				openTime++;
			}
		} while (!isOpenSuccess&&openTime<3);

		return true;
	}

	/**
	 * 显示当前日期和时间，并显示操作注释
	 */
	private void showCurrentTime(String text) {
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String TimeString = time.format(new java.util.Date());
		//System.out.println(TimeString + "--" + text);
		logger.info(TimeString + "--" + text);
	}

	/**
	 * 关闭浏览器
	 */
	public void closeBrowser() {
		showCurrentTime("关闭浏览器");
		driver.quit();
		showCurrentTime("完成关闭浏览器");
	}

	/**
	 * 执行广告：首先打开浏览器，然后调用相应的广告执行class，最后关闭浏览器。 遇到广告带不开时，提示代理死了，更换代理
	 * 
	 * @param offerDao
	 * @param profileDao
	 * @return
	 */
	public boolean executeOffer(OfferDao offerDao, ProfileDao profileDao) {
		try {
			boolean executionSuccess = true;
			this.openBrowser();
			// 必须要加上http协议或https协议，否则会抛出错误：http://stackoverflow.com/questions/22145776/selenium-org-openqa-selenium-webdriverexception-f-queryinterface-is-not-a-fun
			executionSuccess = this.openUrl(offerDao.getUrl());
			if (!executionSuccess) {
				logger.info("代理失效，请更换代理，重新运行程序");
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
	 * 通过网站返回参数得到当前的ip地址（适用于使用程序设置代理情况，如小猪代理）
	 * 
	 * @return
	 */
	public String getIp() throws ProxyDiedException{
		try {
			boolean executionSuccess = true;
			this.openBrowser();
			// 必须要加上http协议或https协议，否则会抛出错误：http://stackoverflow.com/questions/22145776/selenium-org-openqa-selenium-webdriverexception-f-queryinterface-is-not-a-fun
			executionSuccess = this.openUrl("http://myip.dnsomatic.com/");
			String myIP = driver.findElement(By.tagName("body")).getText();
			if (!executionSuccess) {
				//代理失效，请更换代理，重新运行程序
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
				// 必须要加上http协议或https协议，否则会抛出错误：http://stackoverflow.com/questions/22145776/selenium-org-openqa-selenium-webdriverexception-f-queryinterface-is-not-a-fun
				browserOperation.openUrl("http://www.check2ip.com");
				browserOperation.closeBrowser();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
