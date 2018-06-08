package excel2database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ReadExcel {
	
	//读取xls
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
    }
	
	//读取xlsx
	 public static void ReadXlsx(Connection conn,String path,String SheetName,int[] colunm) throws Exception  {
		 
	        File excelFile = new File(path);
	        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(excelFile));
	        XSSFSheet sheet = wb.getSheetAt(wb.getSheetIndex(SheetName));
	        for (Row row : sheet) {
	    		String[] cellvalue = null;
	        	for (int i : colunm){
	        		Cell cell = row.getCell(i);
	                switch (cell.getCellType()) {
	                case Cell.CELL_TYPE_STRING:
	                    cellvalue[i]=cell.getRichStringCellValue().getString();
	                    break;
	                case Cell.CELL_TYPE_NUMERIC:
	                    if (DateUtil.isCellDateFormatted(cell)) {
	                    	cellvalue[i]=String.valueOf(cell.getDateCellValue());
	                    } else {
	                    	cellvalue[i]=String.valueOf(cell.getNumericCellValue());
	                    }
	                    break;
	                case Cell.CELL_TYPE_BOOLEAN:
	                	cellvalue[i]=String.valueOf(cell.getBooleanCellValue());
	                    break;
	                case Cell.CELL_TYPE_FORMULA:
	                	try {  
	                        /* 
	                         * 此处判断使用公式生成的字符串有问题，因为HSSFDateUtil.isCellDateFormatted(cell)判断过程中cell 
	                         * .getNumericCellValue();方法会抛出java.lang.NumberFormatException异常 
	                         */  
	                         if (HSSFDateUtil.isCellDateFormatted(cell)) {  
	                            Date date = cell.getDateCellValue();  
	                            cellvalue[i]=String.valueOf((date.getYear() + 1900) + "-" + (date.getMonth() + 1) +"-" + date.getDate());  
	                            break;  
	                         } else {  
	                        	 cellvalue[i]=String.valueOf(String.valueOf(cell.getNumericCellValue()));  
	                         }  
	                     } catch (IllegalStateException e) {  
	                    	 cellvalue[i]=String.valueOf(cell.getRichStringCellValue());  
	                     }  
	                    break;
	                default:
	                }
	            }
	        }
	 }
	 
	 //读取csv
	 public static void loadFile(Connection conn,String file) throws IOException {
	      // read text file, auto recognize bom marker or use 
	      // system default if markers not found.
	      BufferedReader br = null;
	      UnicodeReader r = new UnicodeReader(new FileInputStream(file), "GBK");
	      try {
	      	 int count = 0;
	      	 int total = 0;
	         br = new BufferedReader(r);
	         String line = br.readLine();
	         line = br.readLine();
	         ArrayList<ArrayList<String>> tempdata = new ArrayList<ArrayList<String>>();
	         while(line!=null) {
	        	 String[] row = line.split(",");
	        	 if (row.length == 18){
					 ArrayList<String> temprow = new ArrayList<String>();
					 for(String temp : row){
						 temprow.add(temp);
					 }
					 int[] col = {8,9,10,11,12,13,14,15,16,17};
					 temprow = ToolsUtils.SelectMax(temprow,col);
					 tempdata.add(temprow);
					 count++;
					 total++;
					 if (count==10000){
					 	System.out.println(total);
					 	count = 0;
					 	SQLUtils.insertdata(conn,"zhifangzhan",tempdata);
					 	tempdata = new ArrayList<ArrayList<String>>();
					 }
	         	}
	         	line = br.readLine();
	        	 if(line == null){
					 SQLUtils.insertdata(conn,"zhifangzhan",tempdata);
				 }
	         }
	      } catch (IOException ex) {
	         throw ex;
	      } finally {
	         try {
	           br.close(); 
	           r.close();
	         } catch (Exception ex) { }
	      }
	   }
	 
	//写入excel
 	public static void AddExcelContents(Workbook workbook,String[] segments,int rownumber,Sheet sheet) {  
        Row row = sheet.createRow(rownumber);  
        for (int i = 0;i < segments.length;i++)
            row.createCell(i).setCellValue(segments[i]);
    }  
 	//存储excel
	public static void SaveExcel(Workbook workbook,String filepath){
		try {  
            File file = new File(filepath);  
            FileOutputStream fileoutputStream = new FileOutputStream(file);  
            workbook.write(fileoutputStream);  
            fileoutputStream.close(); 
            workbook.close(); 
        } catch (IOException e) {  
            e.printStackTrace();  
        } 
	}

}
