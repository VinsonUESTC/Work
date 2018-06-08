package excel;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {
	
	public static void ReadXls(String path,String SheetName,int[] colunm) throws Exception  {
		 
        File excelFile = new File(path);
        HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(excelFile));
        HSSFSheet sheet = wb.getSheetAt(wb.getSheetIndex(SheetName));
        ArrayList values = new ArrayList();
        for (Row row : sheet) {
    		ArrayList cellvalue = new ArrayList();
        	for (int i : colunm){
        		Cell cell = row.getCell(i);
                switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    cellvalue.add(cell.getRichStringCellValue().getString());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                    	cellvalue.add(String.valueOf(cell.getDateCellValue()));
                    } else {
                    	cellvalue.add(cell.getNumericCellValue());
                    }
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                	cellvalue.add(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                	try {  
                        /* 
                         * 此处判断使用公式生成的字符串有问题，因为HSSFDateUtil.isCellDateFormatted(cell)判断过程中cell 
                         * .getNumericCellValue();方法会抛出java.lang.NumberFormatException异常 
                         */  
                         if (HSSFDateUtil.isCellDateFormatted(cell)) {  
                            Date date = cell.getDateCellValue();  
                            cellvalue.add((date.getYear() + 1900) + "-" + (date.getMonth() + 1) +"-" + date.getDate());  
                            break;  
                         } else {  
                        	 cellvalue.add(String.valueOf(cell.getNumericCellValue()));  
                         }  
                     } catch (IllegalStateException e) {  
                    	 cellvalue.add(String.valueOf(cell.getRichStringCellValue()));  
                     }  
                    break;
                default:
                }
            }
            values.add(cellvalue);
        }
        System.out.println(values);
    }
	 public static ArrayList ReadXlsx(String path,String SheetName,int[] colunm) throws Exception  {
		 
	        File excelFile = new File(path);
	        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(excelFile));
	        XSSFSheet sheet = wb.getSheetAt(wb.getSheetIndex(SheetName));
	        ArrayList values = new ArrayList();
	        for (Row row : sheet) {
	    		ArrayList cellvalue = new ArrayList();
	        	for (int i : colunm){
	        		Cell cell = row.getCell(i);
	                switch (cell.getCellType()) {
	                case Cell.CELL_TYPE_STRING:
	                    cellvalue.add(cell.getRichStringCellValue().getString());
	                    break;
	                case Cell.CELL_TYPE_NUMERIC:
	                    if (DateUtil.isCellDateFormatted(cell)) {
	                    	cellvalue.add(String.valueOf(cell.getDateCellValue()));
	                    } else {
	                    	cellvalue.add(cell.getNumericCellValue());
	                    }
	                    break;
	                case Cell.CELL_TYPE_BOOLEAN:
	                	cellvalue.add(cell.getBooleanCellValue());
	                    break;
	                case Cell.CELL_TYPE_FORMULA:
	                	try {  
	                        /* 
	                         * 此处判断使用公式生成的字符串有问题，因为HSSFDateUtil.isCellDateFormatted(cell)判断过程中cell 
	                         * .getNumericCellValue();方法会抛出java.lang.NumberFormatException异常 
	                         */  
	                         if (HSSFDateUtil.isCellDateFormatted(cell)) {  
	                            Date date = cell.getDateCellValue();  
	                            cellvalue.add((date.getYear() + 1900) + "-" + (date.getMonth() + 1) +"-" + date.getDate());  
	                            break;  
	                         } else {  
	                        	 cellvalue.add(String.valueOf(cell.getNumericCellValue()));  
	                         }  
	                     } catch (IllegalStateException e) {  
	                    	 cellvalue.add(String.valueOf(cell.getRichStringCellValue()));  
	                     }  
	                    break;
	                default:
	                }
	            }
	            values.add(cellvalue);
	        }
	        return values;
	 }
	 @SuppressWarnings("rawtypes")
	 //函数
	public static void ReadData(){
		 int[] col = {9,10,11,12,13,14,15,16,17,18,19,20};
		 ArrayList data = new ArrayList();
		 try {
			 data = ReadXlsx("G:\\documents\\分析工作\\创新课题\\直放站巡检\\直放站信息.xlsx","Sheet1",col);
			 for(int i = 0; i < data.size() ; i++){
				 String tempstr = null;
				 String tempmax = "0";
				 ArrayList tempdata = new ArrayList();
				 tempdata = (ArrayList) data.get(i);
				 int j = 0;
				 for (int k = 1; k < tempdata.size(); k++){
					 tempstr = (String) tempdata.get(k);
					 if(Integer.valueOf(tempstr)>Integer.valueOf(tempmax)){
						 tempmax = tempstr;
					 }
				 }
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 public static void main(String[] args) throws Exception  {
		 int[] col1 = {1,2,3};
		 //ReadXls("G:\\documents\\分析工作\\LTE网络集约数据核查工单转派及牵头解决\\20180316165457_LTE网络集约数据核查_20180316_回单（无锡）.xls","疑似天线接反小区",col);
		 //ReadXlsx("G:\\documents\\分析工作\\LTE网络集约数据核查工单转派及牵头解决\\20180316165457_LTE网络集约数据核查_20180316_派单.xlsx","汇总");
		 //ReadXlsx("G:\\documents\\分析工作\\LTE网络集约数据核查工单转派及牵头解决\\lteSector_list.xlsx","lteSector_list",col);
		 ReadData();
	 }
 }

