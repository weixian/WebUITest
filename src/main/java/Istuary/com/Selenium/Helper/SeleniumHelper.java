package Istuary.com.Selenium.Helper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;

import Istuary.com.Web.util.Locator;
import Istuary.com.Web.util.Log;

public class SeleniumHelper {
	protected Log log = new Log(this.getClass());
	protected WebDriver driver;

	public SeleniumHelper(WebDriver driver) {
		this.driver = driver;
	}

	public void type(Locator locator, String values) throws Exception {
		WebElement e = findElement(driver, locator);
		e.sendKeys(values);
	}

	public String getText(Locator locator) throws Exception {
		WebElement e = findElement(driver, locator);
		return e.getText();
	}

	public WebElement findElement(WebDriver driver, final Locator locator) {
		WebElement element = null;
		try {
			element = (new WebDriverWait(driver, locator.getWaitSec())).until(new ExpectedCondition<WebElement>() {

				public WebElement apply(WebDriver driver) {
					try {
						return getElement(driver, locator);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						log.error("can't find element " + locator.getElement());
						return null;
					}

				}

			});

		} catch (TimeoutException e) {
		//	e.printStackTrace();
			log.error("Time out can't find element " + locator.getElement());
		}

		return element;

	}

	public WebElement getElement(WebDriver driver, Locator locator) {
		// locator = getLocator(locator.getElement());
		WebElement e = null;

		try {
			switch (locator.getBy()) {
			case xpath:
				log.debug("find element By xpath");
				e = driver.findElement(By.xpath(locator.getElement()));
				break;
			case id:
				log.debug("find element By id");
				e = driver.findElement(By.id(locator.getElement()));
				break;
			case name:
				log.debug("find element By name");
				e = driver.findElement(By.name(locator.getElement()));
				break;
			case cssSelector:
				log.debug("find element By cssSelector");
				e = driver.findElement(By.cssSelector(locator.getElement()));
				break;
			case className:
				log.debug("find element By className");
				e = driver.findElement(By.className(locator.getElement()));
				break;
			case tagName:
				log.debug("find element By tagName");
				e = driver.findElement(By.tagName(locator.getElement()));
				break;
			case linkText:
				log.debug("find element By linkText");
				e = driver.findElement(By.linkText(locator.getElement()));
				break;
			case partialLinkText:
				log.debug("find element By partialLinkText");
				e = driver.findElement(By.partialLinkText(locator.getElement()));
				break;
			default:
				e = driver.findElement(By.id(locator.getElement()));
			}

		} catch (NoSuchElementException e1) {
			// e1.printStackTrace();
			takeScreenShot();
		}
		return e;

	}

	public boolean isElementPresent(WebDriver driver, Locator myLocator, int timeOut) {
		final Locator locator = myLocator;
		boolean isPresent = false;
		WebDriverWait wait = new WebDriverWait(driver, timeOut);

		try {
			isPresent = wait.until(new ExpectedCondition<WebElement>() {
				public WebElement apply(WebDriver d) {
					return findElement(d, locator);
				}
			}).isDisplayed();

		} catch (TimeoutException e) {

		}

		return isPresent;
	}

	public boolean isElementPresent(Locator locator, int timeOut) {
		return isElementPresent(driver, locator, timeOut);

	}

	protected void scrollToElement(Locator locator) {
		WebElement e = findElement(driver, locator);
		log.info("scroll view element");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// roll down and keep the element to the center of browser
		js.executeScript("arguments[0].scrollIntoViewIfNeeded(true);", e);
	}

	public void click(Locator locator) throws Exception {
		WebElement e = findElement(driver, locator);
		log.info("click button");
		e.click();
	}

	public void select(Locator locator, String value) throws Exception {
		WebElement e = findElement(driver, locator);
		Select select = new Select(e);

		try {
			log.info("select by Value " + value);
			select.selectByValue(value);
		} catch (Exception notByValue) {
			log.info("select by VisibleText " + value);
			select.selectByVisibleText(value);
		}
	}

	public void alertConfirm() throws Exception {
		Alert alert = driver.switchTo().alert();
		try {
			alert.accept();
		} catch (Exception notFindAlert) {
			throw notFindAlert;
		}
	}

	public void alertDismiss() throws Exception {
		Alert alert = driver.switchTo().alert();
		try {
			alert.dismiss();
		} catch (Exception notFindAlert) {
			throw notFindAlert;
		}
	}

	public String getAlertText() throws Exception {
		Alert alert = driver.switchTo().alert();
		try {
			return alert.getText();
		} catch (Exception notFindAlert) {
			throw notFindAlert;
		}
	}

	public void clickAndHold(Locator locator) throws IOException {
		WebElement e = findElement(driver, locator);
		Actions actions = new Actions(driver);
		actions.clickAndHold(e).perform();
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * @author XianWei
	 */
	public void takeScreenShot() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		String dateStr = sf.format(date);
		String path = "/snapShot/"+this.getClass().getSimpleName() + "_" + dateStr + ".png";
		takeScreenShot((TakesScreenshot) this.getDriver(), path);
	}

	/**
	 * @author XianWei
	 * @param drivername
	 * @param path
	 */
	public void takeScreenShot(TakesScreenshot drivername, String path) {
		// this method will take screen shot ,require two parameters ,one is
		// driver name, another is file name
		String currentPath = System.getProperty("user.dir"); // get current work
		log.info(currentPath);
		File scrFile = drivername.getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy
		try {
			log.info("save snapshot path is:" + currentPath + path);
			FileUtils.copyFile(scrFile, new File(currentPath + "\\" + path));
		} catch (Exception e) {
			log.error("Can't save screenshot");
			e.printStackTrace();
		} finally {
			log.info("screen shot finished");
		}
	}

}
