package Istuary.com.Web;

import org.openqa.selenium.WebDriver;
import Istuary.com.Selenium.Helper.DriverFactory;
import Istuary.com.Web.pageAction.UboLoginPage;

public class UboLoginTest {
	public  static void main(String[] agr)
	{
		WebDriver driver = DriverFactory.getChromeDriver();
		
		UboLoginPage login;
		try {
			login = new UboLoginPage(driver);
			
			if(login.login("aaaa", "bbb")!=0)
				System.out.println("Login failed");
			else
				System.out.println("Login success!");
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
