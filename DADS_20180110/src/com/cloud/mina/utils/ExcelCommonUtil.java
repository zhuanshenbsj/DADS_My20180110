package com.cloud.mina.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
public class ExcelCommonUtil {
	 static DecimalFormat df = new DecimalFormat("0");// 格式化 number String字符
	 static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
	public static String getValue(Cell cell){
		 Object value ;
		 switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				value = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				if ("@".equals(cell.getCellStyle().getDataFormatString())) {
					value = df.format(cell.getNumericCellValue());
				} else if ("General".equals(cell.getCellStyle()
						.getDataFormatString())) {
					value = df.format(cell.getNumericCellValue());
				} else {
					value = sdf.format(HSSFDateUtil.getJavaDate(cell
							.getNumericCellValue()));
					value = DateUtil.format(value.toString());
				}
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				value = cell.getBooleanCellValue();
				break;
			case Cell.CELL_TYPE_BLANK:
				value = "";
				break;
			default:
				value = cell.toString();
			}
		 return value.toString();
	 }
}
