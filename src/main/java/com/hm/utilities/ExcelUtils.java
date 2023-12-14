package com.hm.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;

public class ExcelUtils {

	public synchronized static Object[][] readExcel(String filePath, String fileName, String sheetName)
			throws IOException {
		
		System.out.println(filePath);
		System.out.println(fileName);
		System.out.println(sheetName);
		File file = new File(filePath + "\\" + fileName);

		FileInputStream inputStream = new FileInputStream(file);

		Workbook workbook = null;

		String fileExtensionName = fileName.substring(fileName.indexOf("."));

		if (fileExtensionName.equals(".xlsx")) {

			workbook = new XSSFWorkbook(inputStream);

		}

		else if (fileExtensionName.equals(".xls")) {

			workbook = new HSSFWorkbook(inputStream);

		}
		Sheet sheet;
		if (!sheetName.equals("")) {
			sheet = workbook.getSheet(sheetName);
		}else {
			sheet = workbook.getSheetAt(0);
		}

		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();

		Object[][] object = new Object[rowCount][1];

		// Create a loop over all the rows of excel file to read it

		for (int i = 1; i < rowCount + 1; i++) {
			Hashtable<String, Object> hashtable = new Hashtable<String, Object>();

			Row row = sheet.getRow(i);

			for (int j = 0; j < row.getLastCellNum(); j++) {

				if (row.getCell(j).getCellTypeEnum() == CellType.STRING) {

					hashtable.put(sheet.getRow(0).getCell(j).getStringCellValue(), row.getCell(j).getStringCellValue());
				} else {

					System.out.println(j);
					
					System.out.println(sheet.getRow(0).getCell(j).getStringCellValue());
					hashtable.put(sheet.getRow(0).getCell(j).getStringCellValue(),
							row.getCell(j).getNumericCellValue());
				}

			}

			object[i - 1][0] = hashtable;

		}

		return object;

	}

	public synchronized static Object[][] readExcel(String filePath, String fileName) throws IOException {
		File file = new File(filePath + "\\" + fileName);

		FileInputStream inputStream = new FileInputStream(file);

		Workbook workbook = new XSSFWorkbook(inputStream);
		;

		Sheet sheet = workbook.getSheetAt(0);

		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();

		Object[][] object = new Object[rowCount][1];

		// Create a loop over all the rows of excel file to read it

		for (int i = 1; i < rowCount + 1; i++) {
			Hashtable<String, Object> hashtable = new Hashtable<String, Object>();

			Row row = sheet.getRow(i);

			for (int j = 0; j < row.getLastCellNum(); j++) {

				if (row.getCell(j).getCellTypeEnum() == CellType.STRING) {

					hashtable.put(sheet.getRow(0).getCell(j).getStringCellValue(), row.getCell(j).getStringCellValue());
				} else {

					hashtable.put(sheet.getRow(0).getCell(j).getStringCellValue(),
							row.getCell(j).getNumericCellValue());
				}

			}

			object[i - 1][0] = hashtable;

		}

		return object;

	}

}
