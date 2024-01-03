package com.hm.seleniumFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
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

	private static final String OUTPUT_FOLDER = "./build/HtmlReport/";
	private static final String FILE_NAME = "TestExecutionReport.html";

	private static ExtentReports extent;
	public static ThreadLocal<ExtentTest> test = new ThreadLocal<ExtentTest>();

	public static ThreadLocal<String> methodNameFlag = new ThreadLocal<String>();
	public static ThreadLocal<Integer> iteration = new ThreadLocal<Integer>();

	public static ThreadLocal<ExtentTest> logger = new ThreadLocal<ExtentTest>();

	@BeforeSuite
	public void reports() {

		Path path = Paths.get(OUTPUT_FOLDER);
		// if directory exists?
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				// fail to create directory
				e.printStackTrace();
			}
		}

		/*
		 * ExtentReports extentReports = new ExtentReports(); ExtentSparkReporter
		 * reporter = new ExtentSparkReporter(OUTPUT_FOLDER + FILE_NAME);
		 * reporter.config().setReportName("Atlas");
		 * extentReports.attachReporter(reporter); extentReports.setSystemInfo("System",
		 * "Windows"); extentReports.setSystemInfo("Author", "Sagar");
		 * extentReports.setSystemInfo("Build#", "1.1");
		 * extentReports.setSystemInfo("Team", "HM");
		 */

		extent = new ExtentReports();
		ExtentSparkReporter reporter = new ExtentSparkReporter(OUTPUT_FOLDER + FILE_NAME);
		reporter.config().setReportName("Atlas");
		extent.attachReporter(reporter);
		extent.setSystemInfo("System", "Windows");
		extent.setSystemInfo("Author", "Sagar");
		extent.setSystemInfo("Build#", "1.1");
		extent.setSystemInfo("Team", "HM");

		// extentReports.setSystemInfo("ENV NAME", System.getProperty("env"));

		SeleniumWrapper.init_properties();

		SeleniumWrapper.init_properties();
	}

	@Parameters({ "browser" })
	@BeforeMethod
	public void setup(@Optional("chrome") String browserName, Method method, Object[] params)
			throws InterruptedException {

		if (browserName != null) {

			SeleniumWrapper.init_properties().setProperty("browser", browserName);
			if (!SeleniumWrapper.init_properties().containsKey("smartTimeOut")) {
				SeleniumWrapper.init_properties().setProperty("smartTimeOut", "15");
			}
			browser = browserName;
			method.getParameterCount();
			SeleniumWrapper.initBrowser(browserName);
			initTest(method, params);
		}

	}

	public void initTest(Method method, Object[] params) {
		String methodName = method.getName();
		String className = method.getClass().getName();

		int a = params.length;

		method.getParameters();

		if (methodNameFlag.get() == null || (!methodNameFlag.get().equals(methodName))) {

			logger.remove();
			ExtentTest extentTest = extent.createTest(methodName, "Description");
			test.set(extentTest);
			iteration.set(1);
			logger.set(test.get().createNode("Iteration " + iteration.get(), "Data"));

			methodNameFlag.set(methodName);

		} else {

			logger.remove();
			iteration.set(iteration.get().intValue() + 1);
			logger.set(test.get().createNode("Iteration " + iteration.get(), "Data"));

		}
	}

	public void initTest2(ITestResult result, Method method) {
		String methodName = result.getMethod().getMethodName();
		String qualifiedName = result.getMethod().getQualifiedName();
		int last = qualifiedName.lastIndexOf(".");
		int mid = qualifiedName.substring(0, last).lastIndexOf(".");
		String className = qualifiedName.substring(mid + 1, last);

		method.getParameters();

		if (methodNameFlag.get() == null || (!methodNameFlag.get().equals(methodName))) {

			logger.remove();
			ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName(),
					result.getMethod().getDescription());
			test.set(extentTest);
			logger.set(test.get());
			methodNameFlag.set(methodName);

		} else {

			logger.remove();

			logger.set(test.get().createNode("Iteration " + iteration.get() + 1, "Data"));

		}
	}

	public void testReport(ITestResult result) {

		System.out.println(methodName + " started!");

		if (result.getStatus() == 1) {
			logger.get().pass("Test passed");
			logger.get().pass(result.getThrowable(),
					MediaEntityBuilder.createScreenCaptureFromBase64String(SeleniumWrapper.takeScreenshot(),
							result.getMethod().getMethodName()).build());
			logger.get().getModel().setEndTime(getTime(result.getEndMillis()));
		} else if (result.getStatus() == 2) {
			logger.get().fail(result.getThrowable(),
					MediaEntityBuilder.createScreenCaptureFromBase64String(SeleniumWrapper.takeScreenshot(),
							result.getMethod().getMethodName()).build());
			logger.get().getModel().setEndTime(getTime(result.getEndMillis()));

		} else if (result.getStatus() == 3) {
			logger.get().skip(result.getThrowable(),
					MediaEntityBuilder.createScreenCaptureFromBase64String(SeleniumWrapper.takeScreenshot(),
							result.getMethod().getMethodName()).build());
			logger.get().getModel().setEndTime(getTime(result.getEndMillis()));
		}

	}

	private Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	@AfterMethod
	public void tearDown(ITestResult result) {
		testReport(result);
		System.out.println("After Class");
		SeleniumWrapper.wait(2000);
		SeleniumWrapper.closeBrowser();

	}

	@AfterSuite
	public void suite() {
		System.out.println("After SUite");
		extent.flush();
		test.remove();
		;

	}

}
