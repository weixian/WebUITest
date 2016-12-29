package Istuary.com.Web.pageAction;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

import Istuary.com.Web.util.Locator;
import Istuary.com.Web.util.TextUIObject;
import Istuary.com.Web.util.UIObject;

public class UboLoginPage extends BasePage {
	public UboLoginPage(WebDriver driver) throws Exception {
		super(driver);
		// TODO Auto-generated constructor stub
		this.driver = driver;
		driver.get("http://192.168.28.215:4002/login");
	}

	WebDriver driver;
	UIObject obj = null;

	public WebDriver getDriver() {
		return driver;
	}

	Locator loginUserInputBox = getLocator("loginUserInputBox");
	Locator loginPasswordInputBox = getLocator("loginPwdInputBox");
	Locator loginButton = getLocator("loginButton");

	Locator errorNotice = getLocator("erroeNotice");

	public void typeUserInputBox(String email) throws Exception {
		// open("http://www.zhihu.com");
		// switchToLogin();
		obj = new TextUIObject(driver);
		obj.type(loginUserInputBox, email);
	}

	public void typePasswordInputBox(String password) throws Exception {
		obj = new TextUIObject(driver);
		obj.type(loginPasswordInputBox, password);
	}

	public void clickOnLoginButton() throws Exception {

		obj = new TextUIObject(driver);
		obj.click(loginButton);
	}

	public boolean isPrestentErrorNotice() {

		obj = new TextUIObject(driver);
		return obj.isElementPresent(errorNotice, 10);

	}

	public void waitForPageLoad() {
		super.getDriver().manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
	}

	@SuppressWarnings("finally")
	public int login(String user, String password) {

		int result = 99;
		try {
			typeUserInputBox(user);
			typePasswordInputBox(password);
			clickOnLoginButton();
			if (isPrestentErrorNotice()) {
				log.info("login failed!");
				result = -1;
			} else
				result = 0;
			// Thread.sleep(3000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			result = 0;
			e.printStackTrace();
		} finally {
			return result;
		}
	}

}
