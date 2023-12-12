package com.hm.seleniumFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import com.hm.seleniumFactory.SeleniumWrapper;
import com.hm.utilities.ExcelWrite;

public class BaseTest {

	String methodName;
	String userID;
	String className;
	String clientName;
	String browser;
	String tracingID;
	public static String URL;
	int count = 1;
	List<List<String>> valueToWrite = new ArrayList<>();
	public static String USER = "";
	public static String ENV = "";

	@Parameters({ "browser" })
	@BeforeMethod
	public void setup(@Optional("chrome") String browserName) throws InterruptedException {

		if (browserName != null) {

			SeleniumWrapper.init_properties().setProperty("browser", browserName);
			if (!SeleniumWrapper.init_properties().containsKey("smartTimeOut")) {
				SeleniumWrapper.init_properties().setProperty("smartTimeOut", "15");
			}
			browser = browserName;
			SeleniumWrapper.initBrowser(browserName);
			System.out.println("Parameter names:");

		}

	}

	@AfterMethod
	public void tearDown() {

		SeleniumWrapper.closeBrowser();

	}

}
