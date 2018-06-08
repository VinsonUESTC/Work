package excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class ExcelWriter
{
	public static void AddExcelContents(Workbook workbook,String[] segments,int rownumber,Sheet sheet) {  
        Row row = sheet.createRow(rownumber);  
        for (int i = 0;i < segments.length;i++)
            row.createCell(i).setCellValue(segments[i]);
    }  
	public static void SaveExcel(Workbook workbook){
		try {  
            File file = new File("H:\\documents\\分析工作\\LTE网络集约数据核查工单转派及牵头解决\\处理数据\\result.xlsx");  
            FileOutputStream fileoutputStream = new FileOutputStream(file);  
            workbook.write(fileoutputStream);  
            fileoutputStream.close(); 
            workbook.close(); 
        } catch (IOException e) {  
            e.printStackTrace();  
        } 
	}
	public static void ReadText(String pathname,Workbook workbook){
        try { 
            File filename = new File(pathname); // 要读取以上路径的input。txt文件
            BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(filename),"GBK"));
            String line = br.readLine();
            int i = 0;
        	int i_tianxian = 1;
        	int i_jingweidu = 1;
        	int i_guoyuan = 1;
        	int i_guojin = 1;
        	String sheet_name = pathname.substring(pathname.lastIndexOf("\\")+1,pathname.lastIndexOf("."));
    		ArrayList<String[]> sector = CsvFileReader.loadFile("H:\\documents\\分析工作\\LTE网络集约数据核查工单转派及牵头解决\\lteSector_list.csv");
        	if(sheet_name.contains("_")){
				sheet_name = sheet_name.substring(sheet_name.lastIndexOf("_")+1);
    		}
        	if(sheet_name.contains("工参核查列表")){
        		Sheet tianxian = workbook.createSheet("疑似天线接反小区"); 
        		Sheet jingweidu = workbook.createSheet("疑似经纬度错误小区"); 
        		Sheet guoyuan = workbook.createSheet("疑似过远覆盖小区"); 
        		Sheet guojin = workbook.createSheet("疑似过近覆盖小区"); 
            	while (line!= null) {
            		String[] segments = null;
        			segments = line.split("\t");
                    if(i==0){
                    	segments = line.concat("\t\t"+"行政区").split("\t");
                    	AddExcelContents(workbook,segments,i,tianxian);
                    	AddExcelContents(workbook,segments,i,jingweidu);
                    	AddExcelContents(workbook,segments,i,guoyuan);
                    	AddExcelContents(workbook,segments,i,guojin);
                        i++;
                    }else{
                		if(segments[11].contains("天线接反")){
                			for(String[] tempstrs : sector){
                    			if(tempstrs[6].contains(segments[2])){
                    				if(tempstrs[8].contains(segments[3])){
                    					segments = line.concat("\t"+tempstrs[3].substring(1,3)).split("\t");
                    				}
                    			}
                    		}
    	                	AddExcelContents(workbook,segments,i_tianxian,tianxian);
    	                	i_tianxian++;
	                	}
                		if(segments[12].contains("经纬度错误")){
                			for(String[] tempstrs : sector){
                    			if(tempstrs[6].contains(segments[2])){
                    				if(tempstrs[8].contains(segments[3])){
                    					segments = line.concat("\t"+tempstrs[3].substring(1,3)).split("\t");
                    				}
                    			}
                    		}
    	                	AddExcelContents(workbook,segments,i_jingweidu,jingweidu);
    	                	i_jingweidu++;
	                	}
                		if(segments[12].contains("过近覆盖")){
                			for(String[] tempstrs : sector){
                    			if(tempstrs[6].contains(segments[2])){
                    				if(tempstrs[8].contains(segments[3])){
                    					segments = line.concat("\t"+tempstrs[3].substring(1,3)).split("\t");
                    				}
                    			}
                    		}
    	                	AddExcelContents(workbook,segments,i_guojin,guojin);
    	                	i_guojin++;
	                	}
                		if(segments[12].contains("过远覆盖")){
                			for(String[] tempstrs : sector){
                    			if(tempstrs[6].contains(segments[2])){
                    				if(tempstrs[8].contains(segments[3])){
                    					segments = line.concat("\t"+tempstrs[3].substring(1,3)).split("\t");
                    				}
                    			}
                    		}
    	                	AddExcelContents(workbook,segments,i_guoyuan,guoyuan);
    	                	i_guoyuan++;
	                	}
                    }
                    line = br.readLine(); // 一次读入一行数据
                }
        	}
        	else{
	            Sheet sheet = workbook.createSheet(sheet_name); 
	            if(sheet_name.contains("编号错误")){
	            	while (line!= null) {
	                    String[] segments = line.split("\t");
	                    if(line.contains("City")){
	                    	segments = line.concat("\t"+"行政区").split("\t");
	                    	AddExcelContents(workbook,segments,i,sheet);
	                        i++;
	                    }else{
	                    	if(segments.length>2&&segments[1].contains("无锡")){
	                    		for(String[] tempstrs : sector){
	                    			if(tempstrs[6].contains(segments[3])){
	                    				if(tempstrs[8].contains(segments[4])){
	                    					segments = line.concat(tempstrs[3].substring(1,3)).split("\t");
	                    				}
	                    			}
	                    		}
	    	                	AddExcelContents(workbook,segments,i,sheet);
	    	                	i++;
	    	                }
	                    }
	                    line = br.readLine(); // 一次读入一行数据
	                }
	            }else if(sheet_name.contains("邻区参数")){
	            	while (line!= null) {
	                    String[] segments = line.split("\t");
	                    if(line.contains("No.")){
	                    	segments = line.concat("\t"+"行政区").split("\t");
	                    	AddExcelContents(workbook,segments,i,sheet);
	                        i++;
	                    }else{
	                    	if(segments.length>2&&segments[1].contains("无锡")){
	                    		for(int k = 0; k < sector.size();k++){
	                    			String[] tempstrs = sector.get(k);
	                    			String temps = tempstrs[6].substring(1,tempstrs[6].lastIndexOf("\""))+"-"+tempstrs[8].substring(1,tempstrs[8].lastIndexOf("\""));
	                    			if(temps.contains(segments[3]))
                    					segments = line.concat("\t"+tempstrs[3].substring(1,3)).split("\t");
                    			}
	    	                	AddExcelContents(workbook,segments,i,sheet);
	    	                	i++;
	    	                }
	                    }
	                    line = br.readLine(); // 一次读入一行数据
            		}		
            	}
	            else if(sheet_name.contains("同PCI")){
	            	while (line!= null) {
	                    String[] segments = line.split("\t");
	                    if(line.contains("源城市")){
	                    	segments = line.concat("\t"+"行政区").split("\t");
	                    	AddExcelContents(workbook,segments,i,sheet);
	                        i++;
	                    }else{
	                    	if(segments.length>2&&segments[0].contains("无锡")){
	                    		for(int k = 0; k < sector.size();k++){
	                    			String[] tempstrs = sector.get(k);
	                    			String temps = tempstrs[6].substring(1,tempstrs[6].lastIndexOf("\""))+"-"+tempstrs[8].substring(1,tempstrs[8].lastIndexOf("\""));
	                    			if(temps.contains(segments[3]))
                    					segments = line.concat("\t"+tempstrs[3].substring(1,3)).split("\t");
                    			}
	    	                	AddExcelContents(workbook,segments,i,sheet);
	    	                	i++;
	    	                }
	                    }
	                    line = br.readLine(); // 一次读入一行数据
            		}		
            	}
                else if(sheet_name.contains("TAC范围错误")||sheet_name.contains("BandWidth错误")){
	            	while (line!= null) {
	                    String[] segments = line.split("\t");
	                    if(i==0){
	                    	segments = line.concat("\t"+"行政区").split("\t");
	                    	AddExcelContents(workbook,segments,i,sheet);
	                        i++;
	                    }else{
	    	                if(segments.length>2&&segments[1].contains("无锡")){
	    	                	for(String[] tempstrs : sector){
	                    			if(tempstrs[6].contains(segments[3])){
	                    				if(tempstrs[8].contains(segments[4])){
	                    					segments = line.concat("\t"+tempstrs[3].substring(1,3)).split("\t");
	                    				}
	                    			}
	                    		}
	    	                	AddExcelContents(workbook,segments,i,sheet);
	    	                	i++;
	    	                }
	                    }
	                    line = br.readLine(); // 一次读入一行数据
	                }
	            }
                else if(sheet_name.contains("越界错误")){
	            	while (line!= null) {
	                    String[] segments = line.split("\t");
	                    if(i==0){
	                    	segments = line.concat("\t"+"行政区").split("\t");
	                    	AddExcelContents(workbook,segments,i,sheet);
	                        i++;
	                    }else{
	    	                if(segments.length>2&&segments[1].contains("无锡")){
	    	                	for(String[] tempstrs : sector){
	                    			if(tempstrs[6].contains(segments[4])){
	                    				if(tempstrs[8].contains(segments[5])){
	                    					segments = line.concat("\t"+tempstrs[3].substring(1,3)).split("\t");
	                    				}
	                    			}
	                    		}
	    	                	AddExcelContents(workbook,segments,i,sheet);
	    	                	i++;
	    	                }
	                    }
	                    line = br.readLine(); // 一次读入一行数据
	                }
	            }
	            else if(sheet_name.contains("同PCI干扰")||sheet_name.contains("同逻辑根干扰")){
	            	while (line!= null) {
	                    String[] segments = line.split("\t");
	                    if(i==0){
	                    	segments = line.concat("\t"+"行政区").split("\t");
	                    	AddExcelContents(workbook,segments,i,sheet);
	                        i++;
	                    }else{
	    	                if(segments.length>4&&segments[0].contains("无锡")){
	    	                	for(int k = 0; k < sector.size();k++){
	                    			String[] tempstrs = sector.get(k);
	                    			String temps = tempstrs[6].substring(1,tempstrs[6].lastIndexOf("\""))+"-"+tempstrs[8].substring(1,tempstrs[8].lastIndexOf("\""));
	                    			if(segments[3].contains(temps))
                    					segments = line.concat("\t"+tempstrs[3].substring(1,3)).split("\t");
                    			}
	    	                	AddExcelContents(workbook,segments,i,sheet);
	    	                	i++;
	    	                }
	                    }
	                    line = br.readLine(); // 一次读入一行数据
	                }
	            }
	            else {
	            	while (line!= null) {
	                    String[] segments = line.split("\t");
	                    if(i==0){
	                    	AddExcelContents(workbook,segments,i,sheet);
	                        i++;
	                    }else{
	    	                if(segments.length>4&&segments[1].contains("无锡")){
	    	                	AddExcelContents(workbook,segments,i,sheet);
	    	                	i++;
	    	                }
	                    }
	                    line = br.readLine(); // 一次读入一行数据
	                }
	            }
        	}
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	public static String [] getFileName(String path)
    {
        File file = new File(path);
        String [] fileName = file.list();
        return fileName;
    }
	public static void main(String[] args) {
		String path = "H:\\documents\\分析工作\\LTE网络集约数据核查工单转派及牵头解决\\处理数据\\";
		String[] names = getFileName(path);
        Workbook workbook = new XSSFWorkbook();  
        for (int i = 0 ; i < names.length ; i++){
        	if(names[i].contains("xlsx")){}
        	else{
        		ReadText(path+names[i],workbook);
        	}
        }
		SaveExcel(workbook);
		try {
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
