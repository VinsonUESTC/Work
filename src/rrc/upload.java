package rrc;

import excel2database.SQLUtils;
import excel2database.ToolsUtils;
import excel2database.UnicodeReader;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class upload extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String FILE_PATH = "tempfile";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设置响应内容类型
        response.setContentType("text/html;charset=UTF-8");
        // 先实例化一个文件解析器
        CommonsMultipartResolver coMultiResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        // 判断request请求中是否有文件上传
        if (coMultiResolver.isMultipart(request)) {

            List<String> fileUrlList = new ArrayList<String>(); //用来保存文件路径
            // 转换Request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

            // 获得文件，方式二
            // Return an java.util.Iterator of String objects containing the parameter names of the multipart files contained in this request.
            // jsp页面的input标签可以有不同的name属性值
            Iterator<String> fileNames = multiRequest.getFileNames();

            while (fileNames.hasNext()) { //循环遍历
                MultipartFile file = multiRequest.getFile(fileNames.next()); //取出单个文件
                if (!file.isEmpty()) { //这个判断必须要加，下面的操作和单个文件就一样了
                    try {
                        // 获得输入流
                        InputStream in = file.getInputStream();
                        UnicodeReader r = new UnicodeReader(in, "GBK");
                        Connection conn = SQLUtils.CreateSQLConnection();
                        SQLUtils.CreateTables(conn,"test", "rrc", "CREATE TABLE rrc"
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
                                + "primary key(date,BSCnumber,BTSnumber,Cellnumber))");;
                        try {
                            int count = 0;
                            int total = 0;
                            BufferedReader br = new BufferedReader(r);
                            String line = br.readLine();
                            line = br.readLine();
                            ArrayList<ArrayList<String>> tempdata = new ArrayList<ArrayList<String>>();
                            while (line != null) {
                                String[] row = line.split(",");
                                if (row.length == 18) {
                                    ArrayList<String> temprow = new ArrayList<String>();
                                    for (String temp : row) {
                                        temprow.add(temp);
                                    }
                                    int[] col = {8, 9, 10, 11, 12, 13, 14, 15, 16, 17};
                                    temprow = ToolsUtils.SelectMax(temprow, col);
                                    tempdata.add(temprow);
                                    count++;
                                    total++;
                                    if (count == 10000) {
                                        System.out.println(total);
                                        count = 0;
                                        SQLUtils.insertdata(conn, "rrc", tempdata);
                                        tempdata = new ArrayList<ArrayList<String>>();
                                    }
                                }
                                line = br.readLine();
                                if (line == null) {
                                    SQLUtils.insertdata(conn, "rrc", tempdata);
                                }
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                            e.printStackTrace();
                    }
                    response.getWriter().write("complete");
                }
            }
        }
    }

    // 处理 POST 方法请求的方法
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}