package ftp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtils {
    private FTPClient ftpClient = null;
    private String server;
    private int port;
    private String userName;
    private String userPassword;

    public FtpUtils(String server, int port, String userName, String userPassword) {
        this.server = server;
        this.port = port;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    /**
     * 连接服务器
     * @return 连接成功与否 true:成功， false:失败
     */
    public boolean open() {
        if (ftpClient != null && ftpClient.isConnected()) {
            return true;
        }
        try {
            ftpClient = new FTPClient();
            // 连接
            ftpClient.connect(this.server, this.port);
            ftpClient.login(this.userName, this.userPassword);
            setFtpClient(ftpClient);
        	ftpClient.setControlEncoding("UTF-8");
            // 检测连接是否成功
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                this.close();
                System.err.println("FTP server refused connection.");
                System.exit(1);
            }
            System.out.println("open FTP success:" + this.server + ";port:" + this.port + ";name:" + this.userName
                    + ";pwd:" + this.userPassword);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE); // 设置上传模式.binally  or ascii
            return true;
        } catch (Exception ex) {
            this.close();
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 切换到父目录
     * @return 切换结果 true：成功， false：失败
     */
    @SuppressWarnings("unused")
	private boolean changeToParentDir() {
        try {
            return ftpClient.changeToParentDirectory();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 改变当前目录到指定目录
     * @param dir 目的目录
     * @return 切换结果 true：成功，false：失败
     */
    private boolean cd(String dir) {
        try {
            return ftpClient.changeWorkingDirectory(dir);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取目录下所有的文件名称
     * 
     * @param filePath 指定的目录
     * @return 文件列表,或者null
     */
    @SuppressWarnings("unused")
	private FTPFile[] getFileList(String filePath) {
        try {
            return ftpClient.listFiles(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 层层切换工作目录
     * @param ftpPath 目的目录
     * @return 切换结果
     */
    public boolean changeDir(String ftpPath) {
        if (!ftpClient.isConnected()) {
            return false;
        }
        try {
            // 将路径中的斜杠统一
            char[] chars = ftpPath.toCharArray();
            StringBuffer sbStr = new StringBuffer(256);
            for (int i = 0; i < chars.length; i++) {
                if ('\\' == chars[i]) {
                    sbStr.append('/');
                } else {
                    sbStr.append(chars[i]);
                }
            }
            ftpPath = sbStr.toString();
            if (ftpPath.indexOf('/') == -1) {
                // 只有一层目录
                ftpClient.changeWorkingDirectory(new String(ftpPath.getBytes(), "iso-8859-1"));
            } else {
                // 多层目录循环创建
                String[] paths = ftpPath.split("/");
                for (int i = 0; i < paths.length; i++) {
                    ftpClient.changeWorkingDirectory(new String(paths[i].getBytes(), "iso-8859-1"));
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 循环创建目录，并且创建完目录后，设置工作目录为当前创建的目录下
     * @param ftpPath 需要创建的目录
     * @return
     */
    public boolean mkDir(String ftpPath) {
        if (!ftpClient.isConnected()) {
            return false;
        }
        try {
            // 将路径中的斜杠统一
            char[] chars = ftpPath.toCharArray();
            StringBuffer sbStr = new StringBuffer(256);
            for (int i = 0; i < chars.length; i++) {
                if ('\\' == chars[i]) {
                    sbStr.append('/');
                } else {
                    sbStr.append(chars[i]);
                }
            }
            ftpPath = sbStr.toString();
            System.out.println("ftpPath:" + ftpPath);
            if (ftpPath.indexOf('/') == -1) {
                // 只有一层目录
                ftpClient.makeDirectory(new String(ftpPath.getBytes(), "iso-8859-1"));
                ftpClient.changeWorkingDirectory(new String(ftpPath.getBytes(), "iso-8859-1"));
            } else {
                // 多层目录循环创建
                String[] paths = ftpPath.split("/");
                for (int i = 0; i < paths.length; i++) {
                    ftpClient.makeDirectory(new String(paths[i].getBytes(), "iso-8859-1"));
                    ftpClient.changeWorkingDirectory(new String(paths[i].getBytes(), "iso-8859-1"));
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 上传文件到FTP服务器
     * @param localDirectoryAndFileName 本地文件目录和文件名
     * @param ftpFileName 上传到服务器的文件名
     * @param ftpDirectory FTP目录如:/path1/pathb2/,如果目录不存在会自动创建目录
     * @return
     */
    public boolean upload(String localDirectoryAndFileName, String ftpFileName, String ftpDirectory) {
        if (!ftpClient.isConnected()) {
            return false;
        }
        boolean flag = false;
        if (ftpClient != null) {
            File srcFile = new File(localDirectoryAndFileName);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(srcFile);
                // 创建目录
                this.mkDir(ftpDirectory);
                ftpClient.setBufferSize(100000);
                ftpClient.setControlEncoding("UTF-8");
                // 设置文件类型（二进制）
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                // 上传
                flag = ftpClient.storeFile(new String(ftpFileName.getBytes(), "iso-8859-1"), fis);
            } catch (Exception e) {
                this.close();
                e.printStackTrace();
                return false;
            } finally {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("上传文件成功，本地文件名： " + localDirectoryAndFileName + "，上传到目录：" + ftpDirectory + "/" + ftpFileName);
        return flag;
    }

    /**
     * 从FTP服务器上下载文件
     * @param ftpDirectoryAndFileName ftp服务器文件路径，以/dir形式开始
     * @param localDirectoryAndFileName 保存到本地的目录
     * @return
     */
    public boolean get(String ftpDirectoryAndFileName, String localDirectoryAndFileName) {
        if (!ftpClient.isConnected()) {
            return false;
        }
        ftpClient.enterLocalPassiveMode(); // Use passive mode as default
        try {
            // 将路径中的斜杠统一
            char[] chars = ftpDirectoryAndFileName.toCharArray();
            StringBuffer sbStr = new StringBuffer(256);
            for (int i = 0; i < chars.length; i++) {
                if ('\\' == chars[i]) {
                    sbStr.append('/');
                } else {
                    sbStr.append(chars[i]);
                }
            }
            ftpDirectoryAndFileName = sbStr.toString();
            String filePath = ftpDirectoryAndFileName.substring(0, ftpDirectoryAndFileName.lastIndexOf("/"));
            String fileName = ftpDirectoryAndFileName.substring(ftpDirectoryAndFileName.lastIndexOf("/") + 1);
            this.changeDir(filePath);
            ftpClient.retrieveFile(new String(fileName.getBytes(), "iso-8859-1"),
                    new FileOutputStream(localDirectoryAndFileName)); // download
            // file
            System.out.println("从ftp服务器上下载文件：" + ftpDirectoryAndFileName + "， 保存到：" + localDirectoryAndFileName);
            System.out.println(ftpClient.getReplyString()); // check result
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 返回FTP目录下的文件列表
     * @param pathName
     * @return
     */
    public String[] getFileNameList(String pathName) {
        try {
            return ftpClient.listNames(new String(pathName.getBytes("GBK"),"iso-8859-1"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除FTP上的文件
     * @param ftpDirAndFileName 路径开头不能加/，比如应该是test/filename1
     * @return
     */
    public boolean deleteFile(String ftpDirAndFileName) {
        if (!ftpClient.isConnected()) {
            return false;
        }
        try {
            return ftpClient.deleteFile(ftpDirAndFileName);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除FTP目录
     * @param ftpDirectory
     * @return
     */
    public boolean deleteDirectory(String ftpDirectory) {
        if (!ftpClient.isConnected()) {
            return false;
        }
        try {
            return ftpClient.removeDirectory(ftpDirectory);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 关闭链接
     */
    public void close() {
        try {
            if (ftpClient != null && ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
            System.out.println("成功关闭连接，服务器ip:" + this.server + ", 端口:" + this.port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public static void download(FtpUtils f){
    	//遍历全省邻区核查目录下的文件名
    	String temp_name = "0";
        String[] names = f.getFileNameList("全省邻区核查");
        for(String name : names) {
        	if(name.contains("2018")&&!name.contains(".")){
        		if(Integer.parseInt(temp_name)<Integer.parseInt(name.substring(7))){
        			temp_name = name.substring(7);
        		}
        	};
        }
        //获取当前目录最新文件夹
        String[] names_linqu = f.getFileNameList("全省邻区核查/"+temp_name);
        //遍历mdt文件夹
        String mdt_temp = "0";
        names = f.getFileNameList("MDT");
        for(String name : names) {
	    	if(name.contains("2018年")){
	    		if(Integer.parseInt(name.substring(name.lastIndexOf("年")+1,name.lastIndexOf("月")))>Integer.parseInt(mdt_temp))
	    			mdt_temp = name.substring(name.lastIndexOf("年")+1,name.lastIndexOf("月"));
    		}
    	};
    	String[] names_mdt = f.getFileNameList("MDT/2018年"+mdt_temp+"月");
    	String mdt_temp2 = "0";
    	String mdt_temp3 = null;
		for(String name : names_mdt){
			if(name.contains(".")){}
			else if(Integer.parseInt(name.substring(name.lastIndexOf("/")+1,name.lastIndexOf("_")))-Integer.parseInt(mdt_temp2)>0){
				mdt_temp2 = name.substring(name.lastIndexOf("/")+1,name.lastIndexOf("_"));
				mdt_temp3 = name.substring(name.lastIndexOf("/")+1);
			}	
    	}
		String[] names_mdt2 = f.getFileNameList("MDT/2018年"+mdt_temp+"月/"+mdt_temp3);
		int mdt_temp4 = 99;
		String mdt_temp5 = null;
		for(String name : names_mdt2){
			if(name.contains("电信1.8G"))
				if(name.length()<mdt_temp4){
					mdt_temp4 = name.length();
					mdt_temp5 = name;
				}
		}
		String[] names_mdt3 = f.getFileNameList(mdt_temp5+"/无锡");
		String mdt_final = null;
		for (String name : names_mdt3){
			if(name.contains("工参")){
				mdt_final = name;
			}
			
		}
		
        //下载邻区文件
        for (int i = 0 ; i<names_linqu.length; i++){
        	f.get(names_linqu[i], "H:\\documents\\分析工作\\LTE网络集约数据核查工单转派及牵头解决\\下载数据\\"+names_linqu[i].substring(names_linqu[i].lastIndexOf("/")+1));
        	if(names_linqu[i].contains("邻区参数配置错误"))
        		f.get(names_linqu[i], "H:\\documents\\分析工作\\LTE网络集约数据核查工单转派及牵头解决\\处理数据\\邻区参数.txt");
        	else if(names_linqu[i].contains("CellID配置错误"))
        		f.get(names_linqu[i], "H:\\documents\\分析工作\\LTE网络集约数据核查工单转派及牵头解决\\处理数据\\编号错误.txt");
        	else if(names_linqu[i].contains("TAC配置错误"))
        		f.get(names_linqu[i], "H:\\documents\\分析工作\\LTE网络集约数据核查工单转派及牵头解决\\处理数据\\TAC范围错误.txt");
        	else if(names_linqu[i].contains("TAL越界问题"))
        		f.get(names_linqu[i], "H:\\documents\\分析工作\\LTE网络集约数据核查工单转派及牵头解决\\处理数据\\TAC越界错误.txt");
        	else if(names_linqu[i].contains("多扇区同位置"))
        		f.get(names_linqu[i], "H:\\documents\\分析工作\\LTE网络集约数据核查工单转派及牵头解决\\处理数据\\多扇区同位置.txt");
        	else f.get(names_linqu[i], "H:\\documents\\分析工作\\LTE网络集约数据核查工单转派及牵头解决\\处理数据\\"+names_linqu[i].substring(names_linqu[i].lastIndexOf("/")+1,names_linqu[i].lastIndexOf("_"))+".txt");
        }
        f.cd("/"); //返回根目录
        //下载mdt文件
        f.get(mdt_final,"H:\\documents\\分析工作\\LTE网络集约数据核查工单转派及牵头解决\\下载数据\\"+mdt_final.substring(mdt_final.lastIndexOf("/")+1));
        f.get(mdt_final,"H:\\documents\\分析工作\\LTE网络集约数据核查工单转派及牵头解决\\处理数据\\"+mdt_final.substring(mdt_final.lastIndexOf("/")+1,mdt_final.lastIndexOf("_"))+".txt");
        f.cd("/"); //返回根目录
        //下载log文件
        f.get("爱网优工参/直放站室分信息/直放站信息_所有_与施主扇区关联核查.log","H:\\documents\\分析工作\\LTE网络集约数据核查工单转派及牵头解决\\下载数据\\直放站信息_所有_与施主扇区关联核查.log");
        f.get("爱网优工参/直放站室分信息/直放站信息_所有_与室分系统位置核查.log","H:\\documents\\分析工作\\LTE网络集约数据核查工单转派及牵头解决\\下载数据\\直放站信息_所有_与室分系统位置核查.log");
        f.get("爱网优工参/直放站室分信息/直放站信息_所有_与施主扇区关联核查.log","H:\\documents\\分析工作\\LTE网络集约数据核查工单转派及牵头解决\\处理数据\\直放站与施主扇区.log");
        f.get("爱网优工参/直放站室分信息/直放站信息_所有_与室分系统位置核查.log","H:\\documents\\分析工作\\LTE网络集约数据核查工单转派及牵头解决\\处理数据\\室分与直放站.log");
        f.cd("/"); //返回根目录
        f.get("爱网优工参/机房站址信息/机房站址信息_所有_RRU与机房位置核查.log","H:\\documents\\分析工作\\LTE网络集约数据核查工单转派及牵头解决\\下载数据\\机房站址信息_所有_RRU与机房位置核查.log");
        f.get("爱网优工参/机房站址信息/机房站址信息_所有_RRU与机房位置核查.log","H:\\documents\\分析工作\\LTE网络集约数据核查工单转派及牵头解决\\处理数据\\机房站址.log");
        f.cd("/"); //返回根目录
        f.get("/华为/NetWings_LTE/异频测量配置带宽超限列表_华为.txt","H:\\documents\\分析工作\\LTE网络集约数据核查工单转派及牵头解决\\下载数据\\异频测量配置带宽超限列表_华为.txt");
        f.get("/华为/NetWings_LTE/异频测量配置带宽超限列表_华为.txt","H:\\documents\\分析工作\\LTE网络集约数据核查工单转派及牵头解决\\处理数据\\邻区BandWidth错误.txt");
    }
    public static void main(String[] args) {
        FtpUtils f = new FtpUtils("132.228.39.71", 21, "LTEXML", "LTEXML321");
        try {
            if(f.open()) {
                //String fileName = "测试2.txt";
                //上传
                //f.upload("d:/1.txt", fileName, "test1");

                //遍历
                //FTPFile[] list = f.getFileList("");
                //for(FTPFile file : list) {
                    //String name = file.getName();
                    //System.out.println("--" + new String(name.getBytes("iso-8859-1"), "UTF-8"));
                //}
            	//下载文件
            	download(f);
                
                //删除
                //String ftpDirAndFileName = "test1/测试.txt";
                //boolean be = f.deleteFile(new String(ftpDirAndFileName.getBytes(), "iso-8859-1"));
                //System.out.println(be);

                //删除目录
                //boolean delf = f.deleteDirectory("test1");
                //System.out.println(delf);

                f.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
};
