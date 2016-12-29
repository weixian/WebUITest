package Istuary.com.Web.pageAction;

import java.io.IOException;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import Istuary.com.Web.util.Locator;
import Istuary.com.Web.util.Log;
import Istuary.com.Web.util.xmlUtils;

public class BasePage {

	protected WebDriver driver;
	// protected String[][] locatorMap;
	HashMap<String, Locator> locatorMap;
	String path;
	protected Log log = new Log(this.getClass());

	public BasePage(WebDriver driver) throws Exception {
		this.driver = driver;
		log.debug(this.getClass().getCanonicalName());
		log.info(System.getProperty("user.dir"));
		// locatorMap = ReadExcelUtil.getLocatorMap();
		path = System.getProperty("user.dir") + "/UIMap/" + this.getClass().getSimpleName() + ".xml";
		log.info(path);
		
		locatorMap = xmlUtils.readXMLDocument(path, this.getClass().getCanonicalName());
		log.info(this.getClass().getCanonicalName());
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public Locator getLocator(String locatorName) throws IOException {

		Locator locator = new Locator(locatorName);
		if (locatorMap != null) {
			locator = locatorMap.get(locatorName);
		}
		return locator;
	}

}
