package com.cloud.mina.utils;

import java.io.InputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public abstract class AbstractExcelUtil {
   public HSSFWorkbook exportExcel(String excelName,String[] title,List list){
	    HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("data");
		//设置单元格宽度
		sheet.setDefaultColumnWidth(20);
		int rowindex = 0;
		//设置title
		HSSFRow headRow = sheet.createRow(rowindex++);
		for(int i=0;i<title.length;i++){
			headRow.createCell(i).setCellValue(title[i]);
		}
		//赋值
		for(Object obj:list){
		  HSSFRow row = sheet.createRow(rowindex++);
		  setValue(row,obj);
		}
	    return wb;
   }
   public abstract void setValue(HSSFRow row,Object obj);
   
   
   public void importExcel(InputStream is){
		Workbook workbook;
		try {
			workbook = new HSSFWorkbook(is);
			Sheet sheet = workbook.getSheetAt(0);
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				importRecord(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
   }
   
   public abstract void importRecord(Row row); 
   
}
