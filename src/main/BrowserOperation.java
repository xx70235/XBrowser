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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import model.ProxyDao;

public class BrowserOperation {

	private WebDriver driver;
	private FirefoxProfile profile;
	private static String baseUrl="null";
	private static String targetUrl="null";
	ArrayList<String> alList;
	ArrayList<String> agentList;
	
	/**
	 * 初始化浏览器，包括读取火狐浏览器的profiles、设置语言、设置useragent。
	 */
	private void initial() {
		File file = new File("profiles");
		profile = new FirefoxProfile(file);
//		setProxy();
		generateAcceptLangList("acceptLanguage.txt");
		generateAgent("useragent.txt");
		setAcceptLang();
		setAgent();
	}
	
	/**
	 * 打开浏览器
	 * @throws Exception
	 */
	public void openBrowser() throws Exception {
		showCurrentTime("开始初始化");
		initial();
		//setProxy();
		showCurrentTime("开始打开浏览器");
		driver = new FirefoxDriver(profile);

//		baseUrl = "null";
//		driver.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);
	}
	
	public void openBrowser(ProxyDao proxyDao) throws Exception {
		showCurrentTime("开始初始化");
		initial();
		setProxy(proxyDao);
		showCurrentTime("开始打开浏览器");
		driver = new FirefoxDriver(profile);

//		baseUrl = "null";
//		driver.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);
	}
	
	public void generateAcceptLangList(String filepath) {
		alList = new ArrayList<String>();
		BufferedReader bReader;
		try {
			bReader = new BufferedReader(new FileReader(new File(
					"AcceptLanguage.txt")));
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
			bReader = new BufferedReader(new FileReader(new File(
					"useragent.txt")));
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
	public void deleteAllCookies()
	{
		driver.manage().deleteAllCookies();
	}
	
	/**
	 * 打开url，如果50秒内未代开，则中断线程
	 * @param url
	 */
	public void openUrl(final String url)
	{
		 Thread t = new Thread(new Runnable()
		    {
		      public void run()
		      {
		    	  showCurrentTime("开始打开网页");
		        driver.get(Thread.currentThread().getName());
		      }
		    }, url);
		    t.start();
		    try
		    {
		      t.join(5000);
		    }
		    catch (InterruptedException e)
		    { // ignore
		    }
		    if (t.isAlive())
		    { // Thread still alive, we need to abort
//		      logger.warning("Timeout on loading page " + url);
		    	 showCurrentTime("打开网页网页超时");
//		    	System.out.println("timeout on loading page");
		      t.interrupt();
		      showCurrentTime("完成interrupt");
		    }
	}
	
	
	/**
	 * 显示当前日期和时间，并显示操作注释
	 */
	private void showCurrentTime(String text)
	{
	  SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  	  String TimeString = time.format(new java.util.Date());
  	  System.out.println(TimeString+"--"+text);
	}
	/**
	 * 关闭浏览器
	 */
	public void closeBrowser()
	{
		showCurrentTime("关闭浏览器");
		driver.close();
		showCurrentTime("完成关闭浏览器");
	}
	
	public static void main(String[] args)
	{
		BrowserOperation browserOperation = new BrowserOperation();
		try {
			browserOperation.openBrowser();
			//必须要加上http协议或https协议，否则会抛出错误：http://stackoverflow.com/questions/22145776/selenium-org-openqa-selenium-webdriverexception-f-queryinterface-is-not-a-fun
			browserOperation.openUrl("http://www.google.com");
			browserOperation.closeBrowser();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
			
	}
}
