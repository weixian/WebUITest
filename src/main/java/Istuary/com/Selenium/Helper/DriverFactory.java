package Istuary.com.Selenium.Helper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.os.WindowsUtils;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import Istuary.com.Web.util.ConfigUtils;
import Istuary.com.Web.util.Log;

/***
 * 
 * @author XianWei
 *
 */
public class DriverFactory {

	private static String chromedriver;
	private static String fireBug;
	private static Properties p = null;
	private static WebDriver driver = null;
	private static String IEDriverServer;
	private static String EDGEDriver;
	private static ChromeDriverService chromeservice = null;
	private static String config = System.getProperty("user.dir") + "\\config\\config.properties";
	static Log log = new Log(DriverFactory.class);

	// public static WebDriver getHtmlUnit() {
	// HtmlUnitDriver driver = new HtmlUnitDriver();
	// log.info("Create HtmlUnitDrive ");
	// return driver;
	// }

	public static WebDriver getChromeDriver() {

		try {
			p = ConfigUtils.getProperties(config);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		if (p != null) {
			chromedriver = p.getProperty("chromedriver");
			System.out.println(p);
		}

		log.info("chrome driver path is " + chromedriver);

		System.setProperty("webdriver.chrome.driver", chromedriver);

		ChromeOptions options = new ChromeOptions();
		// options.addExtensions(new File(""));
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability("chrome.switches", Arrays.asList("--start-maximized"));
		options.addArguments("--test-type", "--start-maximized");
		// getChromeService();
		// driver = new ChromeDriver();

		driver = new RemoteWebDriver(getChromeService().getUrl(), DesiredCapabilities.chrome());
		log.info("Create ChromeDriver ");
		return driver;
	}

	public static ChromeDriverService getChromeService() {

		try {
			p = ConfigUtils.getProperties(config);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		if (p != null) {
			chromedriver = p.getProperty("chromedriver");
		}

		chromeservice = new ChromeDriverService.Builder().usingDriverExecutable(new File(chromedriver))
				.usingAnyFreePort().build();
		try {
			chromeservice.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return chromeservice;
	}

	public static WebDriver getFirefoxDriver() {
		try {
			WindowsUtils.tryToKillByName("firefox.exe");
			p = ConfigUtils.getProperties(config);

		} catch (Exception e) {
			log.error("can not find firefox process");
		}

		if (p != null) {
			fireBug = p.getProperty("fireBug");
		}

		File file = new File(fireBug);
		FirefoxProfile profile = new FirefoxProfile();
		// profile.setPreference("network.proxy.type", 2);
		// profile.setPreference("network.proxy.autoconfig_url",
		// profile.setPreference("network.proxy.no_proxies_on", "localhost");
		//

		// profile.setPreference("network.proxy.http",
		// "proxy.domain.example.com");
		// profile.setPreference("network.proxy.http_port", 8080);
		// profile.setPreference("network.proxy.ssl",
		// "proxy.domain.example.com");
		// profile.setPreference("network.proxy.ssl_port", 8080);
		// profile.setPreference("network.proxy.ftp",
		// "proxy.domain.example.com");
		// profile.setPreference("network.proxy.ftp_port", 8080);
		// profile.setPreference("network.proxy.socks",
		// "proxy.domain.example.com");
		// profile.setPreference("network.proxy.socks_port", 8080);

		try {
			profile.addExtension(file);
			profile.setPreference("extensions.firebug.currentVersion", "2.0.4");
			profile.setPreference("extensions.firebug.allPagesActivation", "off");
		} catch (IOException e3) {
			e3.printStackTrace();
		}
		profile.setPreference("browser.download.folderList", 2);
		profile.setPreference("browser.download.dir", "C:\\selenium");
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
				"application/octet-stream, application/vnd.ms-excel, text/csv, application/zip,application/exe");
		driver = new FirefoxDriver(profile);
		log.info("Create FirefoxDriver ");
		return driver;

	}

	public static WebDriver getIEDriver() {
		try {
			p = ConfigUtils.getProperties(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (p != null) {
			IEDriverServer = p.getProperty("IEDriverServer");
		}
		System.setProperty("webdriver.ie.driver", IEDriverServer);
		String PROXY = "http://proxy:8083";
		org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
		proxy.setHttpProxy(PROXY).setFtpProxy(PROXY).setSslProxy(PROXY);

		DesiredCapabilities ds = DesiredCapabilities.internetExplorer();
		ds.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		ds.setCapability("ignoreProtectedModeSettings", true);
		ds.setCapability(CapabilityType.PROXY, proxy);
		driver = new InternetExplorerDriver(ds);
		return driver;
	}

	public static WebDriver getEDGEDriver() {
		try {
			p = ConfigUtils.getProperties(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (p != null) {
			EDGEDriver = p.getProperty("EDGEDriver");
		}
		System.setProperty("webdriver.edge.driver", EDGEDriver);
		String PROXY = "https://raw.githubusercontent.com/seveniruby/gfwlist2pac/master/test/proxy.pac";
		org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
		proxy.setHttpProxy(PROXY).setFtpProxy(PROXY).setSslProxy(PROXY);
		DesiredCapabilities capabilities = DesiredCapabilities.edge();
		EdgeOptions options = new EdgeOptions();
		options.setPageLoadStrategy("normal");
		capabilities.setCapability(EdgeOptions.CAPABILITY, options);
		capabilities.setCapability(CapabilityType.PROXY, proxy);
		driver = new EdgeDriver(capabilities);
		return driver;
	}

	public static void exit() {
		if (driver != null)
			driver.close();
		if (chromeservice != null)
			chromeservice.stop();

	}

}
