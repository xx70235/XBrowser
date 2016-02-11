package action;

import java.util.concurrent.TimeUnit;

import model.CardModel;

import org.junit.*;

import static org.junit.Assert.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class NewFinalPageWP {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	private CardModel cardModel;

	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "http://wpengine.com/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	public NewFinalPageWP(WebDriver driver, CardModel model) {
		this.driver = driver;
		this.cardModel = model;
	}

	@Test
	public void emuAction() throws Exception {
		
		
		    driver.findElement(By.id("signup_site_name")).clear();
		    driver.findElement(By.id("signup_site_name")).sendKeys(cardModel.getAccName());
		    driver.findElement(By.id("signup_data_center")).click();
		    new Select(driver.findElement(By.id("signup_data_center"))).selectByVisibleText("United States");
		    driver.findElement(By.id("signup_first_name")).clear();
		    driver.findElement(By.id("signup_first_name")).sendKeys(cardModel.getFname());
		    driver.findElement(By.id("signup_last_name")).clear();
		    driver.findElement(By.id("signup_last_name")).sendKeys(cardModel.getLname());
		    driver.findElement(By.id("signup_email")).clear();
		    driver.findElement(By.id("signup_email")).sendKeys(cardModel.getEmail());
		    driver.findElement(By.id("signup_password")).clear();
		    driver.findElement(By.id("signup_password")).sendKeys(cardModel.getPassword());
		    driver.switchTo().frame(0);
		    // ERROR: Caught exception [ERROR: Unsupported command [selectFrame | wicket-wicket:default | ]]
		    driver.findElement(By.id("cardHolderFirstName")).clear();
		    driver.findElement(By.id("cardHolderFirstName")).sendKeys(cardModel.getFname());
		    driver.findElement(By.id("cardHolderLastName")).clear();
		    driver.findElement(By.id("cardHolderLastName")).sendKeys(cardModel.getLname());
		    new Select(driver.findElement(By.id("cardType"))).selectByVisibleText("VISA");
		    driver.findElement(By.id("cardIdentifierNumber")).clear();
		    driver.findElement(By.id("cardIdentifierNumber")).sendKeys(cardModel.getCardnum());
		    driver.findElement(By.id("cardVerificationNumber")).clear();
		    driver.findElement(By.id("cardVerificationNumber")).sendKeys(cardModel.getCvv());
		    driver.findElement(By.id("cardExpiration")).clear();
		    driver.findElement(By.id("cardExpiration")).sendKeys(cardModel.getMonth() + "/" + cardModel.getYear());
		    driver.findElement(By.id("addressLine1")).clear();
		    driver.findElement(By.id("addressLine1")).sendKeys(cardModel.getAddr());
		    driver.findElement(By.id("addressCity")).clear();
		    driver.findElement(By.id("addressCity")).sendKeys(cardModel.getCity());
//		    new Select(driver.findElement(By.id("state"))).selectByVisibleText("ILLINOIS");
		    driver.findElement(By.id("addressPostalCode")).clear();
		    driver.findElement(By.id("addressPostalCode")).sendKeys(cardModel.getZipcode());
		    // ERROR: Caught exception [ERROR: Unsupported command [selectWindow | null | ]]
//		    driver.findElement(By.id("signup_terms")).click();
		
		
//		driver.findElement(By.id("first_name")).clear();
//		driver.findElement(By.id("first_name")).sendKeys(cardModel.getFname());
//		driver.findElement(By.id("last_name")).clear();
//		driver.findElement(By.id("last_name")).sendKeys(cardModel.getLname());
//		driver.findElement(By.id("email")).clear();
//		driver.findElement(By.id("email")).sendKeys(cardModel.getEmail());
////		driver.findElement(By.id("phone")).clear();
////		driver.findElement(By.id("phone")).sendKeys(cardModel.getTelnum());
//		driver.findElement(By.id("password")).clear();
//
//		driver.findElement(By.id("password")).sendKeys(cardModel.getPassword());
//		driver.findElement(By.id("confirm_password")).clear();
//		driver.findElement(By.id("confirm_password")).sendKeys(
//				cardModel.getPassword());
//		driver.findElement(By.id("account_name")).clear();
//		driver.findElement(By.id("account_name")).sendKeys(
//				cardModel.getAccName());
////		driver.findElement(By.id("howdyahear")).clear();
////		driver.findElement(By.id("howdyahear")).sendKeys("twitter");
//
//		driver.switchTo().frame(0);
//		driver.findElement(By.name("cardHolderFirstName"))
//				.clear();
//		driver.findElement(By.name("cardHolderFirstName"))
//				.sendKeys(cardModel.getFname());
//		driver.findElement(By.name("cardHolderLastName"))
//				.clear();
//		driver.findElement(By.name("cardHolderLastName"))
//				.sendKeys(cardModel.getLname());
//		driver.findElement(
//				By.name("cardIdentifierNumber")).clear();
//		driver.findElement(
//				By.name("cardIdentifierNumber"))
//				.sendKeys(cardModel.getCardnum());
//		driver.findElement(By.name("cardExpiration"))
//				.clear();
//		driver.findElement(By.name("cardExpiration"))
//				.sendKeys(cardModel.getMonth() + "/" + cardModel.getYear());
//		
//		driver.findElement(By.name("cardVerificationNumber")).clear();
//		driver.findElement(By.name("cardVerificationNumber")).sendKeys(cardModel.getCvv());
////		driver.findElement(By.name("personalInfo:emailAddress")).clear();
////		driver.findElement(By.name("personalInfo:emailAddress")).sendKeys(
////				cardModel.getEmail());
//		driver.findElement(By.name("addressLine1"))
//				.clear();
//		driver.findElement(By.name("addressLine1"))
//				.sendKeys(cardModel.getAddr());
//		driver.findElement(By.name("addressCity"))
//				.clear();
//		driver.findElement(By.name("addressCity"))
//				.sendKeys(cardModel.getCity());
//		driver.findElement(By.name("addressPostalCode"))
//				.clear();
//		driver.findElement(By.name("addressPostalCode"))
//				.sendKeys(cardModel.getZipcode());
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
