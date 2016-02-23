package offerOperater;

import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import model.OfferDao;
import model.ProfileDao;
import model.StateNameMap;
import utils.ProxyDiedException;
import utils.StringUtils;

public class ShoppingSocialitesWeb2537 implements OfferOperation{
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Override
  public void setUp(WebDriver driver){
	this.driver = driver;
    baseUrl = "http://www.elitemate.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Override
  public boolean execute(OfferDao offerDao,ProfileDao profileDao) throws UnknownHostException,ProxyDiedException {
    driver.get(baseUrl + "/myJsp/join.jsp?id=shpsc&path=R&pageid=364&subid=313405__");
    driver.findElement(By.id("uname2")).clear();
    driver.findElement(By.id("uname2")).sendKeys(StringUtils.generateUsername(profileDao));
    driver.findElement(By.id("pwd")).clear();
    String password = StringUtils.generatePassword(8);
    driver.findElement(By.id("pwd")).sendKeys(password);
    driver.findElement(By.id("pwd2")).clear();
    driver.findElement(By.id("pwd2")).sendKeys(password);
    driver.findElement(By.id("fname")).clear();
    driver.findElement(By.id("fname")).sendKeys(profileDao.getFirstName());
    driver.findElement(By.id("lname")).clear();
    driver.findElement(By.id("lname")).sendKeys(profileDao.getLastName());
    driver.findElement(By.id("email")).clear();
    driver.findElement(By.id("email")).sendKeys(profileDao.getEmail());
    driver.findElement(By.id("add")).clear();
    driver.findElement(By.id("add")).sendKeys(profileDao.getEmail());
    driver.findElement(By.id("city")).clear();
    driver.findElement(By.id("city")).sendKeys(profileDao.getCity());
    new Select(driver.findElement(By.id("statecombo"))).selectByVisibleText(StateNameMap.getStateFullName(profileDao.getState()));
    driver.findElement(By.id("zip")).clear();
    driver.findElement(By.id("zip")).sendKeys(profileDao.getZipcode());
    driver.findElement(By.id("phPrim1")).clear();
    driver.findElement(By.id("phPrim1")).sendKeys(profileDao.getPhone());
    driver.findElement(By.id("phPrim2")).clear();
    driver.findElement(By.id("phPrim2")).sendKeys("");
    driver.findElement(By.id("phPrim3")).clear();
    driver.findElement(By.id("phPrim3")).sendKeys("");
    driver.findElement(By.id("terms")).click();
    driver.findElement(By.id("digital_signature")).clear();
    driver.findElement(By.id("digital_signature")).sendKeys(profileDao.getFirstName()+""+profileDao.getLastName());
    driver.findElement(By.id("terms1")).click();
    driver.findElement(By.id("digital_signaturetwo")).clear();
    driver.findElement(By.id("digital_signaturetwo")).sendKeys(profileDao.getFirstName()+""+profileDao.getLastName());
    return true;
  }
  
//  public boolean openUrl(final String url) {
//		// 判断打开页面是否成功
//		boolean isOpenSuccess = true;
//		int openTime = 0;
//		do {
//			Thread t = new Thread(new Runnable() {
//				public void run() {
//					showCurrentTime("开始打开网页");
//					driver.get(Thread.currentThread().getName());
//				}
//			}, url);
//			t.start();
//			try {
//				t.join(150000);
//			} catch (InterruptedException e) { // ignore
//			}
//			if (t.isAlive()) { // Thread still alive, we need to abort
//				// logger.warning("Timeout on loading page " + url);
//				showCurrentTime("打开网页网页超时");
//				// System.out.println("timeout on loading page");
//				t.interrupt();
//				showCurrentTime("完成interrupt");
//				return false;
//			}
//			showCurrentTime("打开网页完毕");
//			//
//			//判断当前打开的页面是否包含Unable to connect，如果是则说明当前代理失
//			String tmp = driver.findElement(By.tagName("body")).getText();
//			if (tmp.contains("Unable to connect")||tmp.contains("The proxy server is refusing connections")) {
//				isOpenSuccess = false;
//				openTime++;
//			}
//		} while (!isOpenSuccess&&openTime<3);
//		
//		return isOpenSuccess;
//	}

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
