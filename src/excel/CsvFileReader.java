 package excel;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CsvFileReader {

	public static ArrayList<String[]> loadFile(String file) throws IOException {
	      // read text file, auto recognize bom marker or use 
	      // system default if markers not found.
	      BufferedReader br = null;
	      UnicodeReader r = new UnicodeReader(new FileInputStream(file), "GBK");
	      ArrayList<String[]> Data = new ArrayList<String[]>();
	      try {
	         br = new BufferedReader(r);
	         String line = br.readLine();
	         while(line!=null) {
	        	 String[] row = line.split(",");
	        	 Data.add(row);
	        	 line = br.readLine();
	         }
	      } catch (IOException ex) {
	         throw ex;
	      } finally {
	         try {
	           br.close(); 
	           r.close();
	         } catch (Exception ex) { }
	      }
	         return Data;
	   }

	public static void main(String[] args) throws IOException{
		ArrayList<String[]> data = loadFile("G:\\documents\\分析工作\\LTE网络集约数据核查工单转派及牵头解决\\lteSector_list.csv");
		for (String[] a : data){
				System.out.println(a[8]);
			}
		
	}
}