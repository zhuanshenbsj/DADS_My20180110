package com.cloud.mina.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelWriter {

	public static <T> HSSFWorkbook writeExcel(List<T> list,String sheetName,String sheetTitle,String[] title,String[] params)
	{
		HSSFWorkbook book = null;
		if(list!=null)
		{
			//创建excel
			book = new HSSFWorkbook();
			//创建一个空sheet,凑格式
//			book.createSheet("sheet1");
			//创建药品信息sheet
			HSSFSheet sheet = book.createSheet(sheetName);
//			book.setActiveSheet(1);
			//设定表头样式
			HSSFCellStyle headerStyle = createHeaderstyle(book);
			//设置副表头样式
			HSSFCellStyle secHeaderStyle = createSecHeaderstyle(book);
			//设定标题样式
			HSSFCellStyle titleStyle = createTitleStyle(book);
			//设置普通样式
			HSSFCellStyle normalStyle = createNormalStyle(book);
			//设置列的默认宽度  
	        sheet.setDefaultColumnWidth(20);
	        //合并单元格
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (title.length-1)));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, (title.length-1)));
			//创建Excel表头
			//第一行
			HSSFRow headRow = sheet.createRow(0);
			HSSFCell headCell = headRow.createCell(0);
			headCell.setCellStyle(headerStyle);
			headCell.setCellValue(sheetTitle);
			//第二行
			//创建Excel副表头
			HSSFRow secHeadRow = sheet.createRow(1);
			HSSFCell secHeadCell = secHeadRow.createCell(0);
			secHeadCell.setCellStyle(secHeaderStyle);
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			secHeadCell.setCellValue(formatter.format(date)+"导出");
			//第三行
			//创建Excel标题
			HSSFRow titleRow = sheet.createRow(2);
			for(int i=0;i<title.length;i++)
			{
				HSSFCell titleCell = titleRow.createCell(i);
				titleCell.setCellStyle(titleStyle);
				titleCell.setCellValue(title[i]);
			}
			
			//药品信息
			Iterator<T> iterator = list.iterator();
			int index = 3;
			while(iterator.hasNext())
			{
				T entity = iterator.next();
				HSSFRow row = sheet.createRow(index);
				//导出实体信息
				for(int i=0;i<title.length;i++)
				{
					HSSFCell cell = row.createCell(i);
					cell.setCellStyle(normalStyle);

					try {
						cell.setCellValue(entity.getClass().getDeclaredMethod(params[i]).invoke(entity)+"");
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}
				index++;
			}
		}
		return book;
	}
	
	private static HSSFCellStyle createHeaderstyle(HSSFWorkbook book)
	{
		HSSFCellStyle style = book.createCellStyle();
		//居中
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		//设定此样式的的背景颜色填充样式
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		//设置背景颜色
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		//设置字体
		HSSFFont font = book.createFont();
		font.setFontHeightInPoints((short)22);
		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//设置黑体
		style.setFont(font);
		return style;
	}
	
	private static HSSFCellStyle createSecHeaderstyle(HSSFWorkbook book)
	{
		HSSFCellStyle style = book.createCellStyle();
		//居中
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		//设定此样式的的背景颜色填充样式
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		//设置背景颜色
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		//设置字体
		//设置字体
		HSSFFont font = book.createFont();
		font.setFontHeightInPoints((short)16);
		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//设置黑体
		style.setFont(font);
		return style;
	}
	
	private static HSSFCellStyle createTitleStyle(HSSFWorkbook book)
	{
		HSSFCellStyle style = book.createCellStyle();
		//居中
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		//设定此样式的的背景颜色填充样式
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		//设置背景颜色
		style.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		//设置字体
		HSSFFont font = book.createFont();
		font.setFontHeightInPoints((short)14);
		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//设置加粗
		style.setFont(font);
		return style;
	}

	private static HSSFCellStyle createNormalStyle(HSSFWorkbook book)
	{
		HSSFCellStyle style = book.createCellStyle();
		//居中
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//		//设定此样式的的背景颜色填充样式
//		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//		//设置背景颜色
//		style.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		//设置字体
		HSSFFont font = book.createFont();
		font.setFontHeightInPoints((short)10);
		font.setFontName("宋体");
//		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//设置加粗
		style.setFont(font);
		return style;
	}
}
