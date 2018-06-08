package excel2database;

import java.sql.Connection;

public class Test1 {
	public static void main(String args[]) throws Exception{
		Connection tempconn = SQLUtils.CreateSQLConnection();
		SQLUtils.DropTables(tempconn,"test", "zhifangzhan", "DROP TABLE zhifangzhan");
		SQLUtils.CreateTables(tempconn,"test", "zhifangzhan", "CREATE TABLE zhifangzhan"
				+ "("
				+ "date datetime NOT NULL,"
				+ "city nvarchar(max) NOT NULL,"
				+ "MSCname nvarchar(max) NOT NULL,"
				+ "BSCnumber int NOT NULL,"
				+ "BTSnumber int NOT NULL,"
				+ "Cellnumber int NOT NULL,"
				+ "BSCname nvarchar(max) NOT NULL,"
				+ "Cellname nvarchar(max) NOT NULL,"
				+ "[1km] bigint NOT NULL,"
				+ "[2km] bigint NOT NULL,"
				+ "[3km] bigint NOT NULL,"
				+ "[4km] bigint NOT NULL,"
				+ "[5km] bigint NOT NULL,"
				+ "[6km] bigint NOT NULL,"
				+ "[7km] bigint NOT NULL,"
				+ "[8km] bigint NOT NULL,"
				+ "[9km] bigint NOT NULL,"
				+ "[10km] bigint NOT NULL,"
				+ "maxnumber bigint ,"
				+ "[1kmmax] bit,"
				+ "[2kmmax] bit,"
				+ "[3kmmax] bit,"
				+ "[4kmmax] bit,"
				+ "[5kmmax] bit,"
				+ "[6kmmax] bit,"
				+ "[7kmmax] bit,"
				+ "[8kmmax] bit,"
				+ "[9kmmax] bit,"
				+ "[10kmmax] bit,"
				+ "match bit,"
				+ "primary key(date,BSCnumber,BTSnumber,Cellnumber))");
		/*ReadExcel.loadFile(tempconn,"H:\\documents\\分析工作\\创新课题\\直放站巡检\\直放站4月全网.csv");
		ReadExcel.loadFile(tempconn,"H:\\documents\\分析工作\\创新课题\\直放站巡检\\直放站5月全网.csv");
		ReadExcel.loadFile(tempconn,"H:\\documents\\分析工作\\创新课题\\直放站巡检\\直放站5月-6月全网.csv");
		ReadExcel.loadFile(tempconn,"H:\\documents\\分析工作\\创新课题\\直放站巡检\\直放站5月全网(2).csv");*/
		ReadExcel.loadFile(tempconn,"H:\\documents\\分析工作\\创新课题\\直放站巡检\\直放站全网.csv");
		SQLUtils.confirmdata(tempconn,"confirm","2018-4-1","2018-4-30",500,15);
		SQLUtils.porceed(tempconn,"2018-5-1","2018-6-6");
		SQLUtils.CloseConn(tempconn);
	}
}
