package Istuary.com.Web.util;

import java.io.IOException;

public interface UIObject {

	public String read(Locator locator) throws Exception;

	public void type(Locator locator, String value) throws Exception;

	public void click(Locator locator) throws Exception;

	public void select(Locator locator, String value) throws Exception;

	public void clickAndHold(Locator locator) throws IOException;

	public boolean isElementPresent(Locator profile, int i);

}
