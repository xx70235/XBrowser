package action;

import main.SBrowserSwing;
import model.CardModel;

import org.junit.*;

import static org.junit.Assert.*;

import org.openqa.selenium.*;

public class FirstPageWP {
	private WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	private CardModel cardModel;
//	baseUrl = "http://www.moneyshields.com";
//	targetUrl = "/pid18.php";
	// @Before
	// public void setUp() throws Exception {
	// driver = new FirefoxDriver();
	// baseUrl = "http://wpengine.com";
	// driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	// }

	public FirstPageWP(WebDriver driver, CardModel model) {
		this.driver = driver;
		this.cardModel = model;
		if (SBrowserSwing.baseUrl.equals("null")) {
			SBrowserSwing.baseUrl = "http://www.top-hosting-provider.com";
		}
		if (SBrowserSwing.targetUrl.equals("null")) {
			SBrowserSwing.targetUrl = "/id69.php";
		}
	}

	@Test
	public void emuAction() throws Exception {

		driver.get(SBrowserSwing.baseUrl + SBrowserSwing.targetUrl);		
	}

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
