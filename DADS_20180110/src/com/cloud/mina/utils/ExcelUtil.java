package com.cloud.mina.utils;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.AbstractAttribute;

public class ExcelUtil {
	 static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ExcelUtil.class.getName()); 

	public static void doExportAsXLS(String fileName, int sheetIndex,
			String sheetName, List<?> list, String beanName,
			HttpServletRequest request, HttpServletResponse response) {
		OutputStream os = null;
		WritableWorkbook wwb = null;
		try {
			os = new FileOutputStream(fileName + ".xls");
			wwb = Workbook.createWorkbook(os);
			doConfigExcel(sheetIndex, sheetName, list, beanName, wwb);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("excel导出异常！");
		} finally {
			try {
				wwb.write();
				wwb.close();
				os.close();
			} catch (Exception e) {
				log.error("EXCEL导出出现流关闭异常！");
				e.printStackTrace();
			}
		}
		
	}
	public static void doConfigExcel(int sheetIndex,String sheetName,List<?> list ,String beanName,WritableWorkbook wwb) throws ClassNotFoundException, RowsExceededException, WriteException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		WritableFont header_wf = new WritableFont(WritableFont.TAHOMA, 14, WritableFont.BOLD, false);
		WritableCellFormat header_wcfF = new WritableCellFormat(header_wf);
		WritableFont body_wf = new WritableFont(WritableFont.TAHOMA, 12, WritableFont.NO_BOLD, false);
		WritableCellFormat body_wcfF = new WritableCellFormat(body_wf);
		WritableSheet ws = wwb.createSheet(sheetName, sheetIndex);
		List<Map<String,String>> filedList = getExcelConfig(beanName);
		//add the excel header
		for (int i = 0; i < filedList.size(); i++) {
			Map<String,String> map = (Map<String,String>)filedList.get(i);
			Label labelMain = new Label(i, 0, map.get("header"),header_wcfF);
			ws.addCell(labelMain);
			ws.setColumnView(i, 18); 
		}
		//add the data
		for(int j=0,n=list.size();j<n;j++){
			Class<?> cl = Class.forName(beanName);
			Object obj = list.get(j);
			for (int i = 0,m = filedList.size(); i < m; i++) {
				Map<String,String> map = (Map<String,String>)filedList.get(i);
				Field nameField = cl.getDeclaredField(map.get("name"));
				nameField.setAccessible(true);
				Object inner_obj = nameField.get(obj);
				if(inner_obj==null){
					Label labelMain = new Label(i, j+1, "",body_wcfF);
					ws.addCell(labelMain);
					continue;
				}
				if("String".equalsIgnoreCase(map.get("type"))){
					Label labelMain = new Label(i, j+1, String.valueOf(inner_obj),body_wcfF);
					ws.addCell(labelMain);
				}else if("Double".equalsIgnoreCase(map.get("type"))){
					jxl.write.Number labelN = new jxl.write.Number(i, j+1,Double.valueOf(inner_obj.toString()),body_wcfF); 
					ws.addCell(labelN);
				}else if("Integer".equalsIgnoreCase(map.get("type"))){
					jxl.write.Number labelN = new jxl.write.Number(i, j+1,Integer.valueOf(inner_obj.toString()),body_wcfF); 
					ws.addCell(labelN);
				}else if("Long".equalsIgnoreCase(map.get("type"))){
					jxl.write.Number labelN = new jxl.write.Number(i, j+1,Integer.valueOf(inner_obj.toString()),body_wcfF); 
					ws.addCell(labelN);
				}
				
			}
		}
	}
	private static List<Map<String,String>> getExcelConfig(String beanName){
		 List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		 InputStream fis = ExcelUtil.class.getClassLoader().getResourceAsStream("/com/Config/excel_config.xml");  
		 if(fis!=null){
			 SAXReader saxReader = new SAXReader();  
			 List beanList = null;  
		        try {  
		            Document doc = saxReader.read(fis);  
		            beanList = doc.selectNodes("//beans/bean");  
		            Element bean_element = null;
		            for(Iterator iter = beanList.iterator();iter.hasNext();){  
		                Element element = (Element)iter.next();  
		                AbstractAttribute fild_name = (AbstractAttribute)element.attribute("name");
		                if(beanName.equals(fild_name.getValue())){
		                	bean_element = element;
		                	break;
		                }
		            }
		            if(bean_element != null){
		            	List filed_list = bean_element.selectNodes("filed");
		            	for(Iterator iter = filed_list.iterator();iter.hasNext();){
		            		Element filed_element = (Element)iter.next();
		            		Map<String,String> map = new HashMap<String,String>();
		            		Node name_node= filed_element.selectSingleNode("name");
		            		map.put(name_node.getName(), name_node.getText());
		            		Node header_node= filed_element.selectSingleNode("header");
		            		map.put(header_node.getName(), header_node.getText());
		            		Node type_node= filed_element.selectSingleNode("type");
		            		map.put(type_node.getName(), type_node.getText());
		            		list.add(map);
		            	}
		            }else{
		            	log.error("未找到指定BEAN！");
		            	return null;
		            }
		        } catch (DocumentException e) {  
		            e.printStackTrace();  
		            log.error("未找到指定节点！");
		            return null;
		        }  
			 return list;
		 }else{
			 log.error("未找到配置文件！");
			 return null;
		 }
	}   
}
