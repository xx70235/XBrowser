package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class BrowserOperation {

	private WebDriver driver;
	private FirefoxProfile profile;
	private static String baseUrl="null";
	private static String targetUrl="null";
	ArrayList<String> alList;
	ArrayList<String> agentList;
	
	private void initial() {
		File file = new File("profiles");
		profile = new FirefoxProfile(file);
		setProxy();
		generateAcceptLangList("acceptLanguage.txt");
		generateAgent("useragent.txt");
		setAcceptLang();
		setAgent();
	}
	
	public void openBrowser() throws Exception {
		initial();
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

	public void setProxy() {
		profile.setPreference("network.proxy.type", 1);
		profile.setPreference("network.proxy.socks", "127.0.0.1");
		profile.setPreference("network.proxy.socks_port", 9951);
	}
	
	public void deleteAllCookies()
	{
		driver.manage().deleteAllCookies();
	}
	
	public void openUrl(final String url)
	{
		 Thread t = new Thread(new Runnable()
		    {
		      public void run()
		      {
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
		    	System.out.println("timeout on loading page");
		      t.interrupt();
		    }
	}
	
	public void closeBrowser()
	{
		driver.close();
	}
}
