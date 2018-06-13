package excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class ExcelWriter
{
	public static String[] area = {"崇安","北塘","南长","锡山","新区","滨湖","惠山","江阴","宜兴"};
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
	public static HashMap<String, int[]> ReadText(String pathname, Workbook workbook){
		HashMap<String,int[]> area_count = new HashMap<>();
        try { 
            File filename = new File(pathname); // 要读取以上路径的input。txt文件
            BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(filename),"GBK"));
            String line = br.readLine();
            int i = 0;
        	int i_tianxian = 1;
        	int i_jingweidu = 1;
        	int i_guoyuan = 1;
        	int i_guojin = 1;
        	int length = 0;
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
				int[] count_tianxian = {0,0,0,0,0,0,0,0,0};
				int[] count_jingweidu = {0,0,0,0,0,0,0,0,0};
				int[] count_guoyuan = {0,0,0,0,0,0,0,0,0};
				int[] count_guojin = {0,0,0,0,0,0,0,0,0};
            	while (line!= null) {
					String[] segments_temp = null;
            		String[] segments = null;
            		ArrayList<String> tempdata = new ArrayList<>();
        			segments_temp = line.split("\t");
                    if(i==0){
                    	segments = line.concat("\t"+"行政区").split("\t");
						length = segments.length;
                    	AddExcelContents(workbook,segments,i,tianxian);
                    	AddExcelContents(workbook,segments,i,jingweidu);
                    	AddExcelContents(workbook,segments,i,guoyuan);
                    	AddExcelContents(workbook,segments,i,guojin);
                        i++;
                    }else{
                    	for(int j =0;j<segments_temp.length;j++){
                    		tempdata.add(segments_temp[j]);
						}
						for(int j = 0;j<length-segments_temp.length;j++) {
							tempdata.add(null);
						}
						segments = new String[tempdata.size()];
                    	tempdata.toArray(segments);
                		if(segments_temp[11].contains("天线接反")){
                			for(String[] tempstrs : sector){
                    			if(tempstrs[6].contains(segments_temp[2])){
                    				if(tempstrs[8].contains(segments_temp[3])){
                    					segments[length-1] = tempstrs[3].substring(1,3);
                    					for(int j = 0;j<area.length;j++){
                    						if(tempstrs[3].substring(1,3).equals(area[j])){
												count_tianxian[j]++;
											}
                    					}
                    				}
                    			}
                    		}
                    		if(segments[segments.length-1]==null){
                				for (String[] tempstrs : sector){
									if(tempstrs[6].contains(segments_temp[2])){
										segments[length-1] = tempstrs[3].substring(1,3);
										for(int j = 0;j<area.length;j++){
											if(tempstrs[3].substring(1,3).equals(area[j])){
												count_tianxian[j]++;
											}
										}
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
										segments[length-1] = tempstrs[3].substring(1,3);
										for(int j = 0;j<area.length;j++){
											if(tempstrs[3].substring(1,3).equals(area[j])){
												count_jingweidu[j]++;
											}
										}
                    				}
                    			}
                    		}
							if(segments[segments.length-1]==null){
								for (String[] tempstrs : sector){
									if(tempstrs[6].contains(segments_temp[2])){
										segments[length-1] = tempstrs[3].substring(1,3);
										for(int j = 0;j<area.length;j++){
											if(tempstrs[3].substring(1,3).equals(area[j])){
												count_jingweidu[j]++;
											}
										}
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
										segments[length-1] = tempstrs[3].substring(1,3);
										for(int j = 0;j<area.length;j++){
											if(tempstrs[3].substring(1,3).equals(area[j])){
												count_guojin[j]++;
											}
										}
                    				}
                    			}
                    		}
							if(segments[segments.length-1]==null){
								for (String[] tempstrs : sector){
									if(tempstrs[6].contains(segments_temp[2])){
										segments[length-1] = tempstrs[3].substring(1,3);
										for(int j = 0;j<area.length;j++){
											if(tempstrs[3].substring(1,3).equals(area[j])){
												count_guojin[j]++;
											}
										}
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
										segments[length-1] = tempstrs[3].substring(1,3);
										for(int j = 0;j<area.length;j++){
											if(tempstrs[3].substring(1,3).equals(area[j])){
												count_guoyuan[j]++;
											}
										}
                    				}
                    			}
                    		}
							if(segments[segments.length-1]==null){
								for (String[] tempstrs : sector){
									if(tempstrs[6].contains(segments_temp[2])){
										segments[length-1] = tempstrs[3].substring(1,3);
										for(int j = 0;j<area.length;j++){
											if(tempstrs[3].substring(1,3).equals(area[j])){
												count_guoyuan[j]++;
											}
										}
									}
								}
							}
    	                	AddExcelContents(workbook,segments,i_guoyuan,guoyuan);
    	                	i_guoyuan++;
	                	}
                    }
                    line = br.readLine(); // 一次读入一行数据
                }
				area_count.put(tianxian.getSheetName(),count_tianxian);
				area_count.put(jingweidu.getSheetName(),count_jingweidu);
				area_count.put(guojin.getSheetName(),count_guoyuan);
				area_count.put(guoyuan.getSheetName(),count_guoyuan);
        	}
        	else{
	            Sheet sheet = workbook.createSheet(sheet_name);
	            if(sheet_name.contains("编号错误")){
					int[] count = {0,0,0,0,0,0,0,0,0};
	            	while (line!= null) {
	                    String[] segments = line.split("\t");
	                    if(line.contains("City")){
	                    	segments = line.concat("\t"+"行政区").split("\t");
	                    	AddExcelContents(workbook,segments,i,sheet);
	                        i++;
	                    }else{
	                    	if(segments.length>2&&segments[1].contains("无锡")){
	                    		String area_line = null;
	                    		for(String[] tempstrs : sector){
	                    			if(tempstrs[6].contains(segments[3])){
	                    				if(tempstrs[8].contains(segments[4])){
	                    					area_line = tempstrs[3].substring(1,3);
	                    					segments = line.concat(area_line).split("\t");
											for(int j = 0;j<area.length;j++){
												if(tempstrs[3].substring(1,3).equals(area[j])){
													count[j]++;
												}
											}
	                    				}
	                    			}
	                    		}
	                    		if (area_line == null){
									for(String[] tempstrs : sector){
										if(tempstrs[6].contains(segments[3])){
											area_line = tempstrs[3].substring(1,3);
											segments = line.concat(area_line).split("\t");
											for(int j = 0;j<area.length;j++){
												if(tempstrs[3].substring(1,3).equals(area[j])){
													count[j]++;
												}
											}
										}
									}
								}
	    	                	AddExcelContents(workbook,segments,i,sheet);
	    	                	i++;
	    	                }
	                    }
	                    line = br.readLine(); // 一次读入一行数据
	                }
	                area_count.put(sheet_name,count);
	            }else if(sheet_name.contains("邻区参数")){
					int[] count = {0,0,0,0,0,0,0,0,0};
	            	while (line!= null) {
	                    String[] segments = line.split("\t");
	                    if(line.contains("No.")){
	                    	segments = line.concat("\t"+"行政区").split("\t");
	                    	AddExcelContents(workbook,segments,i,sheet);
	                        i++;
	                    }else{
	                    	if(segments.length>2&&segments[1].contains("无锡")){
	                    		String area_line = null;
	                    		for(String[] tempstrs : sector){
	                    			String temps = tempstrs[6].substring(1,tempstrs[6].lastIndexOf("\""))+"-"+tempstrs[8].substring(1,tempstrs[8].lastIndexOf("\""));
	                    			if(temps.contains(segments[3]))
										area_line = tempstrs[3].substring(1,3);
                    					segments = line.concat("\t"+area_line).split("\t");
                    			}
								if (area_line == null){
									for(String[] tempstrs : sector){
										if(tempstrs[6].contains(segments[3].substring(0,segments[3].indexOf("-")))){
											area_line = tempstrs[3].substring(1,3);
											segments = line.concat(area_line).split("\t");
										}
									}
								}
                    			for(int j = 0;j<area.length;j++){
									if(segments[segments.length-1].equals(area[j])){
										count[j]++;
									}
								}

	    	                	AddExcelContents(workbook,segments,i,sheet);
	    	                	i++;
	    	                }
	                    }
	                    line = br.readLine(); // 一次读入一行数据
            		}
					area_count.put(sheet_name,count);
            	}
	            else if(sheet_name.contains("不存在的目标邻区")){
					int[] count = {0,0,0,0,0,0,0,0,0};
	            	while (line!= null) {
	                    String[] segments = line.split("\t");
	                    if(line.contains("SrcCity")){
	                    	segments = line.concat("\t"+"行政区").split("\t");
	                    	AddExcelContents(workbook,segments,i,sheet);
	                        i++;
	                    }else{
	                    	if(segments.length>2&&segments[1].contains("无锡")){
	                    		String area_line = null;
	                    		for(String[] tempstrs : sector){
	                    			String temps = tempstrs[6].substring(1,tempstrs[6].lastIndexOf("\""))+"-"+tempstrs[8].substring(1,tempstrs[8].lastIndexOf("\""));
	                    			if(temps.contains(segments[3]))
										area_line = tempstrs[3].substring(1,3);
                    					segments = line.concat("\t"+area_line).split("\t");
                    			}
								if (area_line == null){
									for(String[] tempstrs : sector){
										if(tempstrs[6].contains(segments[3].substring(0,segments[3].indexOf("-")))){
											area_line = tempstrs[3].substring(1,3);
											segments = line.concat(area_line).split("\t");
										}
									}
								}
								for(int j = 0;j<area.length;j++){
									if(segments[segments.length-1].equals(area[j])){
										count[j]++;
									}
								}
	    	                	AddExcelContents(workbook,segments,i,sheet);
	    	                	i++;
	    	                }
	                    }
	                    line = br.readLine(); // 一次读入一行数据
            		}
					area_count.put(sheet_name,count);
            	}
                else if(sheet_name.contains("TAC范围错误")||sheet_name.contains("BandWidth错误")){
					int[] count = {0,0,0,0,0,0,0,0,0};
	            	while (line!= null) {
	                    String[] segments = line.split("\t");
	                    if(i==0){
	                    	segments = line.concat("\t"+"行政区").split("\t");
	                    	AddExcelContents(workbook,segments,i,sheet);
	                        i++;
	                    }else{
	    	                if(segments.length>2&&segments[1].contains("无锡")){
	    	                	String area_line = null;
	    	                	for(String[] tempstrs : sector){
	                    			if(tempstrs[6].contains(segments[3])){
	                    				if(tempstrs[8].contains(segments[4])){
	                    					area_line = tempstrs[3].substring(1,3);
	                    					segments = line.concat("\t"+area_line).split("\t");
											for(int j = 0;j<area.length;j++){
												if(tempstrs[3].substring(1,3).equals(area[j])){
													count[j]++;
												}
											}
	                    				}
	                    			}
	                    		}
								if(area_line == null){
									for(String[] tempstrs : sector){
										if(tempstrs[6].contains(segments[3])){
											area_line = tempstrs[3].substring(1,3);
											segments = line.concat("\t"+area_line).split("\t");
											for(int j = 0;j<area.length;j++){
												if(tempstrs[3].substring(1,3).equals(area[j])){
													count[j]++;
												}
											}
										}
									}
								}
	    	                	AddExcelContents(workbook,segments,i,sheet);
	    	                	i++;
	    	                }
	                    }
	                    line = br.readLine(); // 一次读入一行数据
	                }
					area_count.put(sheet_name,count);
	            }
                else if(sheet_name.contains("越界错误")){
					int[] count = {0,0,0,0,0,0,0,0,0};
	            	while (line!= null) {
	                    String[] segments_temp = line.split("\t");
	                    String[] segments = null;
	                    if(i==0){
	                    	segments = line.concat("\t"+"行政区").split("\t");
	                    	AddExcelContents(workbook,segments,i,sheet);
	                    	length = segments.length;
	                        i++;
	                    }else{
							ArrayList<String> tempdata = new ArrayList<>();
	                    	for(int j = 0; j< segments_temp.length;j++){
	                    		tempdata.add(segments_temp[j]);
							}
							for(int j = 0; j<length-segments_temp.length;j++){
	                    		tempdata.add(null);
							}
							segments = new String[tempdata.size()];
							tempdata.toArray(segments);
	    	                if(segments_temp.length>2&&segments_temp[1].contains("无锡")){
	    	                	String area_line = null;
	    	                	for(String[] tempstrs : sector){
	                    			if(tempstrs[6].contains(segments[4])){
	                    				if(tempstrs[8].contains(segments[5])){
	                    					area_line = tempstrs[3].substring(1,3);
	                    					segments[tempdata.size()-1] = area_line;
											for(int j = 0;j<area.length;j++){
												if(tempstrs[3].substring(1,3).equals(area[j])){
													count[j]++;
												}
											}
	                    				}
	                    			}
	                    		}
	                    		if(area_line == null){
									for(String[] tempstrs : sector){
										if(tempstrs[6].contains(segments[4])){
											area_line = tempstrs[3].substring(1,3);
											segments[tempdata.size()-1] = area_line;
											for(int j = 0;j<area.length;j++){
												if(tempstrs[3].substring(1,3).equals(area[j])){
													count[j]++;
												}
											}
										}
									}
								}
	    	                	AddExcelContents(workbook,segments,i,sheet);
	    	                	i++;
	    	                }
	                    }
	                    line = br.readLine(); // 一次读入一行数据
	                }
					area_count.put(sheet_name,count);
	            }
	            else if(sheet_name.contains("同PCI干扰")||sheet_name.contains("同逻辑根干扰")){
					int[] count = {0,0,0,0,0,0,0,0,0};
	            	while (line!= null) {
	                    String[] segments = line.split("\t");
	                    if(i==0){
	                    	segments = line.concat("\t"+"行政区").split("\t");
	                    	AddExcelContents(workbook,segments,i,sheet);
	                        i++;
	                    }else{
	    	                if(segments.length>4&&segments[0].contains("无锡")){
	    	                	String area_line = null;
	    	                	for(String[] tempstrs : sector){
	                    			String temps = tempstrs[6].substring(1,tempstrs[6].lastIndexOf("\""))+"-"+tempstrs[8].substring(1,tempstrs[8].lastIndexOf("\""));
	                    			if(segments[3].contains(temps))
	                    				area_line = tempstrs[3].substring(1,3);
                    					segments = line.concat("\t"+area_line).split("\t");
                    			}
                    			if(area_line == null){
									for(String[] tempstrs : sector){
										if(segments[3].contains(tempstrs[3].substring(1,3))){
											area_line = tempstrs[3].substring(1,3);
											segments = line.concat("\t"+area_line).split("\t");
										}
									}
								}
                    			for(int j = 0;j<area.length;j++){
									if(segments[segments.length-1].equals(area[j])){
										count[j]++;
									}
								}
	    	                	AddExcelContents(workbook,segments,i,sheet);
	    	                	i++;
	    	                }
	                    }
	                    line = br.readLine(); // 一次读入一行数据
	                }
					area_count.put(sheet_name,count);
	            }
	            else {
					int[] count = {0,0,0,0,0,0,0,0,0};
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
							for(int j = 0;j<area.length;j++){
								if(segments[2].contains(area[j])){
									count[j]++;
								}
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
		return area_count;
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
        ArrayList<HashMap<String,int[]>> sum = new ArrayList<>();
        for (int i = 0 ; i < names.length ; i++){
        	if(names[i].contains("xlsx")){}
        	else{
        		sum.add(ReadText(path+names[i],workbook));
        	}
        }
        Sheet sum_data = workbook.createSheet("汇总");
		int j = 1;
		String[] area_title = new String[area.length+1];
		area_title[0]="行政区";
		for(int m = 1;m<area.length+1;m++){
			area_title[m] = area[m-1];
		}
		AddExcelContents(workbook,area_title,0,sum_data);
        for (HashMap<String,int[]> temp:sum){
        	if(temp.size()>0){
        		for(Object name: temp.keySet().toArray()){
        			ArrayList<String> tempdata = new ArrayList<>();
        			tempdata.add(name.toString());
        			for(int k : temp.get(name.toString())){
        				tempdata.add(String.valueOf(k));
					}
					AddExcelContents(workbook,(String[]) tempdata.toArray(new String[tempdata.size()]),j,sum_data);
        			j++;
				}
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
