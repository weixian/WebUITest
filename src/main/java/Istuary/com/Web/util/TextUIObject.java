package Istuary.com.Web.util;

import java.io.IOException;

import org.openqa.selenium.WebDriver;

import Istuary.com.Selenium.Helper.SeleniumHelper;

public class TextUIObject implements UIObject {

	SeleniumHelper helper;
	protected Log log = new Log(this.getClass());

	public TextUIObject(WebDriver driver) {
		helper = new SeleniumHelper(driver);
	}

	public String read(Locator locator) throws Exception {
		// TODO Auto-generated method stub
		return helper.getText(locator);
	}

	public void type(Locator locator, String value) throws Exception {
		// TODO Auto-generated method stub
		log.info("type  " + value + " to " + locator.getElement());
		helper.type(locator, value);
	}

	public void click(Locator locator) throws Exception {
		// TODO Auto-generated method stub
		log.info("Click " + locator.getElement());
		helper.click(locator);
	}

	public void select(Locator locator, String value) throws Exception {
		// TODO Auto-generated method stub
		helper.select(locator, value);
	}

	public void clickAndHold(Locator locator) throws IOException {
		// TODO Auto-generated method stub
		log.info("Click and hold " + locator.getElement());
		helper.clickAndHold(locator);
	}

	public boolean isElementPresent(Locator locator, int timeOut) {
		return helper.isElementPresent(locator, timeOut);

	}

	public boolean isElementPresent(WebDriver driver, Locator myLocator, int timeOut) throws IOException {

		return helper.isElementPresent(driver, myLocator, timeOut);
	}
}