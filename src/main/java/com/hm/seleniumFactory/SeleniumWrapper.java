package com.hm.seleniumFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.logging.Level;

import com.aventstack.extentreports.MediaEntityBuilder;

import com.hm.enums.WaitStrategy;
import com.hm.listeners.ExtentReportListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class SeleniumWrapper {

	private static WebDriver driver;

	static Properties properties;

	private static ThreadLocal<WebDriver> localDriver = new ThreadLocal<>();

	private static ThreadLocal<JavascriptExecutor> executor = new ThreadLocal<>();

	private static ThreadLocal<TakesScreenshot> screeShot = new ThreadLocal<>();

	private static ThreadLocal<Actions> actions = new ThreadLocal<>();

	public static WebDriver getlocalDriver() {
		return localDriver.get();
	}

	public static JavascriptExecutor getexecutor() {
		return executor.get();
	}

	public static void openWebsite(String url, String pageName) {
		getlocalDriver().get(url);

		getlocalDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));

		new WebDriverWait(getlocalDriver(), Duration.ofSeconds(15)).until(webDriver -> ((JavascriptExecutor) webDriver)
				.executeScript("return document.readyState").equals("complete"));

		ExtentReportListener.test.get().info("SuccesFully Page >>" + pageName + "<< is opened ",
				MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());

	}

	public static synchronized WebElement webElementManger(Object locator, WaitStrategy Strategy) {
		WebElement element = null;
		if (locator instanceof WebElement) {
			element = waitForElement((WebElement) locator, Strategy);
		} else if (locator instanceof By) {
			element = waitForElement((By) locator, Strategy);
		} else {

			wait(5000);
			element = getLocatorJs((String) locator);

		}
		return element;

	}

	public static synchronized WebElement webElementManger(Object locator, WaitStrategy Strategy, int timeOut) {
		WebElement element = null;
		if (locator instanceof WebElement) {
			element = waitForElement((WebElement) locator, Strategy);
		} else if (locator instanceof By) {
			element = waitForElement((By) locator, Strategy);
		} else {

			wait(5000);
			element = getLocatorJs((String) locator);

		}
		return element;

	}

	public static synchronized WebElement waitForElement(By locator, WaitStrategy Strategy, int timeOut) {

		WebElement result = null;

		switch (Strategy) {
		case CLICKABLE:
			result = new WebDriverWait(localDriver.get(), Duration.ofSeconds(timeOut))
					.until(ExpectedConditions.elementToBeClickable(locator));
			break;
		case VISIBLE:
			result = new WebDriverWait(localDriver.get(), Duration.ofSeconds(timeOut))
					.until(ExpectedConditions.visibilityOfElementLocated(locator));
			break;

		case PRESENCE:
			result = new WebDriverWait(localDriver.get(),
					Duration.ofSeconds(Integer.parseInt(getProperties("smartTimeOut"))))
							.until(ExpectedConditions.presenceOfElementLocated(locator));

			break;

		default:
			break;
		}

		return result;

	}

	public static synchronized WebElement waitForElement(By locator, WaitStrategy Strategy) {

		WebElement result = null;

		switch (Strategy) {
		case CLICKABLE:
			result = new WebDriverWait(localDriver.get(), Duration.ofSeconds(30))
					.until(ExpectedConditions.elementToBeClickable(locator));
			break;
		case VISIBLE:
			result = new WebDriverWait(localDriver.get(), Duration.ofSeconds(30))
					.until(ExpectedConditions.visibilityOfElementLocated(locator));
			break;

		case PRESENCE:
			result = new WebDriverWait(localDriver.get(),
					Duration.ofSeconds(Integer.parseInt(getProperties("smartTimeOut"))))
							.until(ExpectedConditions.presenceOfElementLocated(locator));

		default:
			break;
		}

		return result;

	}

	public static synchronized WebElement waitForElement(WebElement locator, WaitStrategy Strategy, int timeOut) {

		WebElement result = null;

		switch (Strategy) {
		case CLICKABLE:
			result = new WebDriverWait(localDriver.get(), Duration.ofSeconds(timeOut))
					.until(ExpectedConditions.elementToBeClickable(locator));
			break;
		case VISIBLE:
			result = new WebDriverWait(localDriver.get(), Duration.ofSeconds(timeOut))
					.until(ExpectedConditions.visibilityOf(locator));
			break;

		default:
			break;
		}

		return result;

	}

	public static synchronized WebElement waitForElement(WebElement locator, WaitStrategy Strategy) {

		WebElement result = null;

		switch (Strategy) {
		case CLICKABLE:
			result = new WebDriverWait(localDriver.get(),
					Duration.ofSeconds(Integer.parseInt(getProperties("smartTimeOut"))))
							.until(ExpectedConditions.elementToBeClickable(locator));
			break;
		case VISIBLE:
			result = new WebDriverWait(localDriver.get(),
					Duration.ofSeconds(Integer.parseInt(getProperties("smartTimeOut"))))
							.until(ExpectedConditions.visibilityOf(locator));

			break;

		default:
			break;
		}

		return result;

	}

	public static synchronized void click(Object locator, String locatorName) {

		try {

			WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);

			highLightWebElementInScreenSchot(element, locatorName);

			ExtentReportListener.test.get().info("Clicking onElement -->" + locatorName,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), locatorName).build());

			element.click();

			unHighLightWebElementInScreenSchot(element);

		} catch (Exception e) {

			WebElement element = webElementManger(locator, WaitStrategy.PRESENCE);
			scrollToElement(locator, locatorName);
			highLightWebElementInScreenSchot(element, locatorName);

			ExtentReportListener.test.get().info("Clicking onElement -->" + locatorName,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), locatorName).build());

			element.click();

			unHighLightWebElementInScreenSchot(element);
		}

	}

	public static synchronized void click(Object locator) {

		try {

			WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);

			highLightWebElementInScreenSchot(element);

			element.click();

			unHighLightWebElementInScreenSchot(element);

		} catch (Exception e) {

			WebElement element = webElementManger(locator, WaitStrategy.PRESENCE);
			scrollToElement(locator);
			highLightWebElementInScreenSchot(element);

			element.click();

			unHighLightWebElementInScreenSchot(element);
		}

	}

	public static synchronized void uploadFile(String locator, String fileName) {

		try {

			String elementPath = locator + "//input[@type='file']";
			WebElement element = webElementManger(elementPath, WaitStrategy.PRESENCE);
			executor.get().executeScript("arguments[0].style.display='block';", element);
			String a = System.getProperty("user.dir") + "\\src\\test\\resources\\"
					+ SeleniumWrapper.getProperties("UploadFilesPath") + "\\" + fileName;
			element.sendKeys(a);

		} catch (Exception e) {

			String elementPath = "(" + locator + "//input[@type='file'])[2]";
			WebElement element = webElementManger(elementPath, WaitStrategy.PRESENCE);
			executor.get().executeScript("arguments[0].style.display='block';", element);
			String a = System.getProperty("user.dir") + "\\src\\test\\resources\\"
					+ SeleniumWrapper.getProperties("UploadFilesPath") + "\\" + fileName;
			element.sendKeys(a);
		}

	}

	public static synchronized void clearTextBox(Object locator, String locatorName) {

		try {

			WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);

			highLightWebElementInScreenSchot(element, locatorName);
			element.clear();
			ExtentReportListener.test.get().info("Clearing Text in Element -->" + locatorName,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), locatorName).build());

			unHighLightWebElementInScreenSchot(element);

		} catch (Exception e) {

			WebElement element = webElementManger(locator, WaitStrategy.PRESENCE);
			scrollToElement(locator, locatorName);
			highLightWebElementInScreenSchot(element, locatorName);

			element.clear();

			ExtentReportListener.test.get().info("Clearing Text in Element -->" + locatorName,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), locatorName).build());
			unHighLightWebElementInScreenSchot(element);
		}

	}

	public static synchronized void selectByVisibleText(Object locator, String value, String locatorName) {

		try {

			WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);

			highLightWebElementInScreenSchot(element, locatorName);

			Select select = new Select(element);

			select.selectByVisibleText(value);

			ExtentReportListener.test.get().info(
					"SelectingValue from Selector" + locatorName + " and selectedValue is " + value,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), locatorName).build());

			unHighLightWebElementInScreenSchot(element);

		} catch (Exception e) {

			WebElement element = webElementManger(locator, WaitStrategy.PRESENCE);

			highLightWebElementInScreenSchot(element, locatorName);

			Select select = new Select(element);

			select.selectByVisibleText(value);

			ExtentReportListener.test.get().info(
					"SelectingValue from Selector" + locatorName + " and selectedValue is " + value,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), locatorName).build());

			unHighLightWebElementInScreenSchot(element);

		}

	}

	public static synchronized void selectByValue(Object locator, String value, String locatorName) {

		try {

			WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);

			highLightWebElementInScreenSchot(element, locatorName);

			Select select = new Select(element);

			select.selectByValue(value);

			ExtentReportListener.test.get().info(
					"SelectingValue from Selector" + locatorName + " and selectedValue is " + value,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), locatorName).build());

			unHighLightWebElementInScreenSchot(element);

		} catch (Exception e) {

			WebElement element = webElementManger(locator, WaitStrategy.PRESENCE);

			highLightWebElementInScreenSchot(element, locatorName);

			Select select = new Select(element);

			select.selectByValue(value);

			ExtentReportListener.test.get().info(
					"SelectingValue from Selector" + locatorName + " and selectedValue is " + value,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), locatorName).build());

			unHighLightWebElementInScreenSchot(element);

		}

	}

	public static synchronized void selectByIndex(Object locator, int value, String locatorName) {

		try {

			WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);

			highLightWebElementInScreenSchot(element, locatorName);

			Select select = new Select(element);

			select.selectByIndex(value);

			ExtentReportListener.test.get().info(
					"SelectingValue from Selector" + locatorName + " and selectedValue is " + value,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), locatorName).build());

			unHighLightWebElementInScreenSchot(element);

		} catch (Exception e) {

			WebElement element = webElementManger(locator, WaitStrategy.PRESENCE);

			highLightWebElementInScreenSchot(element, locatorName);

			Select select = new Select(element);

			select.selectByIndex(value);

			ExtentReportListener.test.get().info(
					"SelectingValue from Selector" + locatorName + " and selectedValue is " + value,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), locatorName).build());

			unHighLightWebElementInScreenSchot(element);

		}

	}

	public static synchronized void clickAndHitBackSPace(Object locator) {

		WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE,
				Integer.parseInt(getProperties("smartTimeOut")));

		element.sendKeys(Keys.BACK_SPACE);

	}

	public static synchronized void clickAndHitEnter(By locator) {

		WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE,
				Integer.parseInt(getProperties("smartTimeOut")));

		element.sendKeys(Keys.ENTER);

	}

	public static synchronized void clickAndHitDelete(By locator) {

		WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE,
				Integer.parseInt(getProperties("smartTimeOut")));

		element.sendKeys(Keys.DELETE);

	}

	public static synchronized void clickAndHitClear(By locator) {

		WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE,
				Integer.parseInt(getProperties("smartTimeOut")));

		element.sendKeys(Keys.CLEAR);

	}

	public static synchronized WebElement clickActiveElement() {

		WebElement element = webElementManger(getActiveElement(), WaitStrategy.CLICKABLE,
				Integer.parseInt(getProperties("smartTimeOut")));

		click(element, "ActiveElement");

		return element;

	}

	public static synchronized void enterTextInActiveElement(String value) {

		WebElement element = webElementManger(getActiveElement(), WaitStrategy.CLICKABLE,
				Integer.parseInt(getProperties("smartTimeOut")));

		enterText(element, value, "ActiveElement");

	}

	public static synchronized void enterText(Object locator, String value, String locatorName) {

		WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE,
				Integer.parseInt(getProperties("smartTimeOut")));

		try {

			element.sendKeys(value);
			highLightWebElementInScreenSchot(element, locatorName);

			ExtentReportListener.test.get().info(
					"Entered text to Element -->" + locatorName + " enteredValue is ==>" + value,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), locatorName).build());

			unHighLightWebElementInScreenSchot(element);

		} catch (Exception e) {

			try {
				scrollToElement(element, locatorName);
				executor.get().executeScript("arguments[0].click();", element);
				highLightWebElementInScreenSchot(element, locatorName);
				element.sendKeys(value);
			} catch (Exception e2) {
				enterValueByJs(element, value, locatorName);
			}

			ExtentReportListener.test.get().info(
					"Entered text to Element -->" + locatorName + " enteredValue is ==>" + value,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), locatorName).build());

			unHighLightWebElementInScreenSchot(element);
		}

	}

	public static synchronized void enterValueByJs(Object locator, String value, String locatorName) {

		WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE,
				Integer.parseInt(getProperties("smartTimeOut")));
		try {

			executor.get().executeScript("arguments[0].value='" + value + "'", element);

			highLightWebElementInScreenSchot(element, locatorName);

			ExtentReportListener.test.get().info(
					"Entered text to Element -->" + locatorName + " enteredValue is ==>" + value,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), locatorName).build());

			unHighLightWebElementInScreenSchot(element);

		} catch (Exception e) {

			executor.get().executeScript("arguments[0].click();", element);

			executor.get().executeScript("arguments[0].value='" + value + "'", element);

			highLightWebElementInScreenSchot(element, locatorName);

			ExtentReportListener.test.get().info(
					"Entered text to Element -->" + locatorName + " enteredValue is ==>" + value,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), locatorName).build());

			unHighLightWebElementInScreenSchot(element);
		}

	}

	public static synchronized void enterJsByValueByNamuber(Object locator, String value, String locatorName) {

		WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE,
				Integer.parseInt(getProperties("smartTimeOut")));

		try {

			highLightWebElementInScreenSchot(element, locatorName);

			executor.get().executeScript("arguments[0].valueAsNumber='" + value + "'", element);

			ExtentReportListener.test.get().info(
					"Entered text to Element -->" + locatorName + " enteredValue is ==>" + value,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), locatorName).build());

			unHighLightWebElementInScreenSchot(element);

		} catch (Exception e) {

			highLightWebElementInScreenSchot(element, locatorName);

			executor.get().executeScript("arguments[0].click();", element);

			executor.get().executeScript("arguments[0].valueAsNumber='" + value + "'", element);

			ExtentReportListener.test.get().info(
					"Entered text to Element -->" + locatorName + " enteredValue is ==>" + value,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), locatorName).build());

			unHighLightWebElementInScreenSchot(element);
		}

	}

	public static synchronized WebElement getLocatorJs(String locator) {

		System.out.println("var element=document.evaluate(\"" + locator
				+ "\", document, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null).snapshotItem(0); return element;");

		return (WebElement) executor.get().executeScript("var element=document.evaluate(\"" + locator
				+ "\", document, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null).snapshotItem(0); return element;");

	}

	public static synchronized void clickJs(Object locator, String locatorName) {

		try {

			WebElement element = webElementManger(locatorName, WaitStrategy.CLICKABLE);

			highLightWebElementInScreenSchot(element, locatorName);

			ExtentReportListener.test.get().info("Clicking onElement -->" + locatorName,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), locatorName).build());

			executor.get().executeScript("arguments[0].click();", element);

			unHighLightWebElementInScreenSchot(element);

		} catch (Exception e) {

			WebElement element = webElementManger(locatorName, WaitStrategy.VISIBLE);
			scrollToElement(element, locatorName);

			highLightWebElementInScreenSchot(element, locatorName);

			ExtentReportListener.test.get().info("Clicking onElement -->" + locatorName,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), locatorName).build());

			executor.get().executeScript("arguments[0].click();", element);
			unHighLightWebElementInScreenSchot(element);
		}

	}

	public static synchronized void scrollToElement(Object locator, String locatorName) {
		WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);

		executor.get().executeScript("arguments[0].scrollIntoView();", element);
		wait(2000);
		highLightWebElementInScreenSchot(element, locatorName);
		ExtentReportListener.test.get().info("Clicking scrollingElemet -->" + locatorName,
				MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), locatorName).build());
		unHighLightWebElementInScreenSchot(element);

	}

	public static synchronized String getText(Object locator, String locatorName) {
		WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);
		highLightWebElementInScreenSchot(element);
		String savedValue = "";
		savedValue = element.getText();
		if (savedValue.equals("")) {
			savedValue = element.getAttribute("value");
			if (savedValue.equals("")) {
				savedValue = element.getAttribute("placeholder");
			}
		}
		ExtentReportListener.test.get().info("Saving value from Locator -->" + locatorName,
				MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), locatorName).build());
		unHighLightWebElementInScreenSchot(element);
		return savedValue;

	}

	public static synchronized void scrollToElement(Object locator) {
		WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);

		executor.get().executeScript("arguments[0].scrollIntoView();", element);
		wait(2000);
		highLightWebElementInScreenSchot(element);

		unHighLightWebElementInScreenSchot(element);

	}

	public static synchronized WebElement getActiveElement() {
		return localDriver.get().switchTo().activeElement();

	}

	public static void wait(int timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static synchronized boolean isVisible(Object locator, String locatorName) {

		boolean result = false;

		try {

			WebElement element = webElementManger(locator, WaitStrategy.VISIBLE, 10);

			result = true;
			highLightWebElementInScreenSchot(element, locatorName);
			ExtentReportListener.test.get().info("Given Element is Visble -->" + locatorName,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), locatorName).build());

		} catch (Exception e) {

			ExtentReportListener.test.get().info("Given Element is  Not Visble -->" + locatorName,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), locatorName).build());
		}

		return result;

	}

	public static synchronized boolean isNotVisible(Object locator, String locatorName) {

		boolean result = false;

		try {

			WebElement element = webElementManger(locator, WaitStrategy.VISIBLE, 10);

			if (element == null) {
				ExtentReportListener.test.get()
						.pass("Given Element is  Not Visble and Step is Passed -->" + locatorName);
				result = true;
			} else {
				ExtentReportListener.test.get().fail(
						"Given Element is Should not be Visble but the Element is Visble hence failing the  Step -->"
								+ locatorName);

			}

		} catch (Exception e) {
			ExtentReportListener.test.get().pass("Given Element is  Not Visble and Step is Passed -->" + locatorName);
			result = true;
		}

		return result;

	}

	public static synchronized void doubleClick(Object locator, String locatorName) {

		WebElement element = webElementManger(locatorName, WaitStrategy.CLICKABLE);
		highLightWebElementInScreenSchot(element, locatorName);

		ExtentReportListener.test.get().info("Clicking onElement -->" + locatorName,
				MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), locatorName).build());

		actions.get().doubleClick().perform();

		unHighLightWebElementInScreenSchot(element);

	}

	public static synchronized void contextClick(Object locator, String locatorName) {

		WebElement element = webElementManger(locatorName, WaitStrategy.CLICKABLE);
		highLightWebElementInScreenSchot(element, locatorName);

		ExtentReportListener.test.get().info("Clicking onElement -->" + locatorName,
				MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), locatorName).build());

		actions.get().contextClick().perform();

		unHighLightWebElementInScreenSchot(element);

	}

	public static synchronized WebDriver initBrowser(String browserName) {

		switch (browserName.toLowerCase()) {
		case "chrome":
			ChromeOptions option = new ChromeOptions();
			option.addArguments("incognito");
			/*
			 * LoggingPreferences logPrefs = new LoggingPreferences();
			 * logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
			 * option.setCapability(ChromeOptions.LOGGING_PREFS, logPrefs);
			 */
			if (SeleniumWrapper.getProperties("headless").equalsIgnoreCase("true")) {
				option.addArguments("headless");
			}
			;
			if (SeleniumWrapper.getProperties("executionMode").equalsIgnoreCase("local")) {
				driver = new ChromeDriver(option);
			} else {
				try {
					driver = new RemoteWebDriver(new URL(SeleniumWrapper.getProperties("hubUrl")), option);
				} catch (MalformedURLException e) {

					System.out.println("Issue in connecting to Remote WebDriver");
				}
			}

			localDriver.set(driver);
			break;
		case "firefox":
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--inprivate");
			driver = new FirefoxDriver(options);
			localDriver.set(driver);
			break;
		case "edge":
			EdgeOptions edgeOptions = new EdgeOptions();
			edgeOptions.addArguments("-inprivate");
			if (SeleniumWrapper.getProperties("headless").equalsIgnoreCase("true")) {
				edgeOptions.addArguments("headless");
			}
			driver = new EdgeDriver(edgeOptions);
			localDriver.set(driver);
			break;

		case "safari":
			// Create SafariOptions
			SafariOptions safari = new SafariOptions();

			// Launch Safari browser with the desired options
			WebDriver driver = new SafariDriver(safari);
			localDriver.set(driver);
			break;

		default:
			System.out.println("please pass the right browser name......");
			break;
		}

		localDriver.get().manage().window().maximize();

		executor.set((JavascriptExecutor) localDriver.get());

		screeShot.set((TakesScreenshot) localDriver.get());

		actions.set(new Actions(localDriver.get()));

		localDriver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		return localDriver.get();

	}

	/**
	 * this method is used to initialize the properties from config file
	 */
	public static synchronized Properties init_properties() {

		try {
			FileInputStream fileInputStream = new FileInputStream("./src/test/resources/config/config.properties");
			properties = new Properties();
			properties.load(fileInputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return properties;
	}

	public static synchronized String getProperties(String key) {

		String value = properties.getProperty(key).trim();
		return value;
	}

	/**
	 * take screenshot
	 * 
	 */

	public static synchronized String takeScreenshot() {

		String SrcFile = screeShot.get().getScreenshotAs(OutputType.BASE64);

		return SrcFile;

	}

	public static synchronized void closeBrowser() {

		localDriver.get().close();

	}

	public static synchronized void highLightWebElementInScreenSchot(WebElement element, String locatorName) {

		try {

			executor.get().executeScript("arguments[0].setAttribute('style', 'border: 6px solid green;');", element);

		} catch (Exception e) {

		}

	}

	public static synchronized void highLightWebElementInScreenSchot(WebElement element) {

		try {

			executor.get().executeScript("arguments[0].setAttribute('style', 'border: 6px solid green;');", element);

		} catch (Exception e) {

		}

	}

	public static synchronized void unHighLightWebElementInScreenSchot(WebElement element) {

		try {
			executor.get().executeScript("arguments[0].setAttribute('style', 'border:');", element);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static String getUtcDate() {
		LocalDateTime utcTime = LocalDateTime.now(ZoneOffset.UTC);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
		return utcTime.format(formatter).toString();

	}

	public static String getLocalDate() {
		LocalDateTime utcTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM");
		return utcTime.format(formatter).toString();

	}

	public static String getUtcTime() {
		LocalDateTime utcTime = LocalDateTime.now(ZoneOffset.UTC);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return utcTime.format(formatter).toString();

	}

	public static String getLocalTime() {
		LocalDateTime utcTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh-mm a");
		return utcTime.format(formatter).toString();

	}

	public static String getUtcDateTimeStamp() {
		LocalDateTime utcTime = LocalDateTime.now(ZoneOffset.UTC);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy  HH:mm:ss");
		return utcTime.format(formatter).toString();

	}

}
