package com.hm.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWrite {

	public static void main(String[] args) throws IOException {

		List<List<String>> valueToWrite = new ArrayList<>();
		List<String> rowData1 = new ArrayList<>();
		rowData1.add("UserName");
		rowData1.add("PassWord");
		List<String> rowData2 = new ArrayList<>();
		rowData2.add("Sagar");
		rowData2.add("Happy@123");

		valueToWrite.add(rowData1);
		valueToWrite.add(rowData2);

		writeExcel(System.getProperty("user.dir"), "ExportExcel.xlsx", "ExcelDemo", valueToWrite);

	}

	public static void writeExcel(String filePath, String fileName, String sheetName, List<List<String>> dataToWrite)
			throws IOException {

		Workbook Workbook = new XSSFWorkbook();

		Sheet sheet = Workbook.createSheet(sheetName);

		// Get the current count of rows in excel file

		for (int i = 0; i < dataToWrite.size(); i++) {
			Row newRow = sheet.createRow(i);

			for (int j = 0; j < dataToWrite.get(i).size(); j++) {

				Cell cell = newRow.createCell(j);
				cell.setCellValue(dataToWrite.get(i).get(j));
			}
		}

		Path path = Paths.get(filePath);

		if (!Files.exists(path)) {

			Files.createDirectories(path);
		}

		// Create an object of FileOutputStream class to create write data in excel file

		FileOutputStream outputStream = new FileOutputStream(filePath + "\\" + fileName);

		// write data in the excel file

		Workbook.write(outputStream);

		// close output stream

		outputStream.close();

	}

}
