package excel2database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SQLUtils {
	
	static String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	static String dbURL = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=test";
	static String userName = "test";
	static String userPwd = "jyf123456";

	public static Connection CreateSQLConnection(){
		Connection conn = null;
		try {
			System.out.println("Connection Start...");
			Class.forName(driverName);
			conn = DriverManager.getConnection(dbURL, userName, userPwd ); 
			System.out.println("successful!");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	//录入数据
	public static void insertdata(Connection conn ,String table ,ArrayList<ArrayList<String>> data)
	{
		PreparedStatement pst = null;
		try {
			conn.setAutoCommit(false);
			String tempdata = "";
			for(int i = 0;i< data.get(0).size() ; i++)
				tempdata += "?,";
			tempdata = tempdata.substring(0,tempdata.length()-1);
			pst = conn.prepareStatement("INSERT INTO [dbo].["+table+"] VALUES ("+tempdata+")");
			for (ArrayList<String> temp : data){
				for(int i = 0; i<temp.size();i++){
					pst.setString(i+1,temp.get(i));
				}
				pst.addBatch();
			}
			pst.executeBatch();
			pst.close();
			conn.setAutoCommit(true);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	//判断数据
	public static void confirmdata(Connection conn ,String table ,String start_date ,String end_date,int maxnum,int min_confirm)
	{
		DropTables(conn,"test", table, "DROP TABLE "+table);
		PreparedStatement pst = null;
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement("SELECT zhifangzhan.BSCnumber,zhifangzhan.BTSnumber,zhifangzhan.Cellnumber,\n" +
					"\tavg(zhifangzhan.[1km])as [1kmavg],avg(zhifangzhan.[2km]) as [2kmavg],avg(zhifangzhan.[3km]) as [3kmavg],\n" +
					"\tavg(zhifangzhan.[4km]) as [4kmavg],avg(zhifangzhan.[5km]) as [5kmavg],avg(zhifangzhan.[6km]) as [6kmavg],\n" +
					"\tavg(zhifangzhan.[7km]) as [7kmavg],avg(zhifangzhan.[8km]) as [8kmavg],avg(zhifangzhan.[9km]) as [9kmavg],avg(zhifangzhan.[10km]) as [10kmavg],\n" +
					"\tsum(CAST(zhifangzhan.[1kmmax]as int)) as [1kmsum],sum(CAST(zhifangzhan.[2kmmax]as int)) as [2kmsum],sum(CAST(zhifangzhan.[3kmmax]as int)) as [3kmsum],\n" +
					"\tsum(CAST(zhifangzhan.[4kmmax] as int)) as [4kmsum],\tsum(CAST(zhifangzhan.[5kmmax]as int)) as [5kmsum],sum(CAST(zhifangzhan.[6kmmax]as int)) as [6kmsum],\n" +
					"\tsum(CAST(zhifangzhan.[7kmmax]as int)) as [7kmsum],sum(CAST(zhifangzhan.[8kmmax] as int)) as [8kmsum],\n" +
					"\tsum(CAST(zhifangzhan.[9kmmax]as int)) as [9kmsum],sum(CAST(zhifangzhan.[10kmmax] as int)) as [10kmsum] into confirm\n" +
					"\t\t\t\t\tFROM [test].[dbo].[zhifangzhan]  \n" +
					"\t\t\t\t\tWHERE zhifangzhan.date between ? and ?  \n" +
					"\t\t\t\t\tAND zhifangzhan.maxnumber > ? \n" +
					"\t\t\t\t\tgroup by zhifangzhan.Cellnumber,zhifangzhan.BTSnumber,zhifangzhan.BSCnumber \n" +
					"\t\t\t\t\thaving (sum(CAST(zhifangzhan.match as int)) >= ?)");
			pst.setString(1,start_date);
			pst.setString(2,end_date);
			pst.setInt(3,maxnum);
			pst.setInt(4,min_confirm);
			pst.execute();
			pst.close();
			conn.setAutoCommit(true);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	//处理数据
	public static void porceed(Connection conn ,String start_date ,String end_date)
	{
		PreparedStatement pst = null;
		ResultSet rs = null;
		int match_count = 0;
		String cellname = null;
		HashMap<String,int[]> cell_feature = new HashMap<String,int[]>();
		ArrayList<String> cell_data = new ArrayList<String>();
		int[] col = {9,10,11,12,13,14,15,16,17,18};
		int[] feature_origin = {0,0,0,0,0,0,0,0,0,0};
		int count = 0;
		try {
		    Statement st = conn.createStatement();
		    ResultSet rspre = st.executeQuery("SELECT *  FROM [test].[dbo].[confirm]");
            while(rspre.next()){
                int[] feature_temp = {0,0,0,0,0,0,0,0,0,0};
                for(int i = 0;i<feature_temp.length;i++){
                    feature_temp[i] = rspre.getInt(i+4);
                }
                cell_feature.put(rspre.getString(1)+"_"+rspre.getString(2)+"_"+rspre.getString(3),feature_temp);
            }
			conn.setAutoCommit(false);
			pst = conn.prepareStatement("SELECT *"+
					"  FROM [test].[dbo].[zhifangzhan] inner join [test].[dbo].confirm on zhifangzhan.Cellnumber = confirm.Cellnumber  " +
					"  and zhifangzhan.BTSnumber = confirm.BTSnumber and zhifangzhan.BSCnumber = confirm.BSCnumber " +
					"  where zhifangzhan.date between ? and ?  " +
					"  order by zhifangzhan.BSCnumber,zhifangzhan.BTSnumber,zhifangzhan.Cellnumber,zhifangzhan.date");
			pst.setString(1,start_date);
			pst.setString(2,end_date);
			rs = pst.executeQuery();
			while(rs.next()){
				if ((rs.getString(4)+"_"+rs.getString(5)+"_"+rs.getString(6)).equals(cellname)){
					if (rs.getInt(30) == 0){
                        for(int i =0;i<col.length;i++){
                            if (feature_origin[i]>50){
                                if (rs.getInt(col[i])*10<feature_origin[i]){
                                    match_count++;
                                }
                            }
                        }
						if(match_count>=3){
                                String tempdata = cellname+"于"+rs.getString(1)+"可能发生问题";
                                cell_data.add(tempdata);
						}
					}else if(rs.getInt(30)==1){
						match_count = 0;
					}
				}else{
					cellname = rs.getString(4)+"_"+rs.getString(5)+"_"+rs.getString(6);
					match_count = 0;
					feature_origin = cell_feature.get(cellname);
				}
			}
			pst.close();
			conn.setAutoCommit(true);
			for (String temp : cell_data){
				System.out.println(temp);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}


	//判断表是否存在
	public static boolean JudgeTableExist(Connection conn,String BaseName ,String TableName) {
		boolean flag = false;
		try {  
			DatabaseMetaData meta = conn.getMetaData();  
			ResultSet rsTables = meta.getTables(BaseName, "dbo", TableName,  new String[] { "TABLE" });  
			flag = rsTables.next();
			rsTables.close();  
		} catch (Exception e) {  
			e.printStackTrace();  
		} 
		return flag;
	}
	
	//创建表
	public static void CreateTables(Connection conn,String BaseName ,String TableName ,String sql){
		Statement stmt = null;
		//先判断表是否存在
		if(JudgeTableExist(conn,BaseName,TableName)==false){
			try {  
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);
				stmt.close();
				System.out.println("Create table:"+TableName+"...Successful!");
			} catch (Exception e) {  
				e.printStackTrace();  
			}
		}
		else{
			System.out.println("table: "+TableName+" already exist!");
		}
	}
	
	//删除表
	public static void DropTables(Connection conn,String BaseName ,String TableName ,String sql){
		Statement stmt = null;
		//先判断表是否存在
		if(JudgeTableExist(conn,BaseName,TableName)==true){
			try {  
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);
				stmt.close();
				System.out.println("Drop table:"+TableName+"...Successful!");
			} catch (Exception e) {  
				e.printStackTrace();  
			}
		}
		else{
			System.out.println("table: "+TableName+" already dropped!");
		}
	}
	//关闭连接
	public static void CloseConn(Connection conn){
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
