package com.hm.utilities;

import java.io.IOException;
import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.DataProvider;

import com.hm.annotations.FrameworkAnnotation;
import com.hm.seleniumFactory.BaseTest;
import com.hm.seleniumFactory.SeleniumWrapper;

public class DataUtil {

	@DataProvider(name = "Data")
	public Object[][] sendData(Method method) throws IOException {

		method.getAnnotation(FrameworkAnnotation.class).dataSheetName();

		return ExcelUtils.readExcel(
				System.getProperty("user.dir") + SeleniumWrapper.getProperties("ExcelfilePath").trim(),
				SeleniumWrapper.getProperties("fileName").trim(),
				method.getAnnotation(FrameworkAnnotation.class).dataSheetName());

	}

}
