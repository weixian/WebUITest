package Istuary.com.Web.pageAction;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

import Istuary.com.Web.util.Locator;
import Istuary.com.Web.util.TextUIObject;
import Istuary.com.Web.util.UIObject;


public class LoginPage extends BasePage {

	WebDriver driver;
	UIObject obj=null;
	
	public WebDriver getDriver() {
		return driver;
	}

	public LoginPage(WebDriver driver) throws Exception {
		super(driver);
		this.driver=driver;
		driver.get("http://www.zhihu.com/#signin");
	}

	
	Locator loginEmailInputBox =getLocator("loginEmailInputBox");
	Locator loginPasswordInputBox = getLocator("loginPasswordInputBox");
	Locator loginButton =getLocator("loginButton");
	Locator profile = getLocator(
			"profile");

	public void typeEmailInputBox(String email) throws Exception {
	//	open("http://www.zhihu.com");
	//	switchToLogin();
		obj=new TextUIObject(driver);
		obj.type(loginEmailInputBox, email);
	}

	public void typePasswordInputBox(String password) throws Exception {
		obj=new TextUIObject(driver);
		obj.type(loginPasswordInputBox, password);
	}

	public void clickOnLoginButton() throws Exception {
		
		obj=new TextUIObject(driver);
		obj.click(loginButton);
	}

	public boolean isPrestentProfile() throws IOException {
		obj=new TextUIObject(driver);
		return obj.isElementPresent(profile, 20);

	}

	public void waitForPageLoad() {
		super.getDriver().manage().timeouts()
				.pageLoadTimeout(30, TimeUnit.SECONDS);
	}

//	public void switchToLogin() throws Exception
//	{
//		obj=new TextUIObject(driver);
//		obj.click(clickToLoginButton);
//	}
	
}
