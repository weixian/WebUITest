package Istuary.com.Web;

import org.openqa.selenium.WebDriver;
import Istuary.com.Selenium.Helper.DriverFactory;
import Istuary.com.Web.pageAction.LoginPage;

public class loginTest {
	
	public  static void main(String[] agr)
	{
		WebDriver driver = DriverFactory.getChromeDriver();
		
//		
//			    driver.get("https://www.zhihu.com/");
//			    TextUIObject obj=new TextUIObject(driver);
//			    
//			    obj.click(new Locator("登录",10,ByType.linkText));
			    
		LoginPage login;
		try {
			login = new LoginPage(driver);
			login.typeEmailInputBox("13282774643");
			login.typePasswordInputBox("appium123");
			login.clickOnLoginButton();
				    Thread.sleep(3000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			   DriverFactory.exit();
		}
		
			 
			    
	}
	  
}
