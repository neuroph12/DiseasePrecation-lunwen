import com.oracle.javafx.jmx.json.JSONDocument;
import com.oracle.javafx.jmx.json.JSONFactory;
import com.oracle.javafx.jmx.json.JSONWriter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.Calendar;
import java.util.Properties;

/**
 * Created by novas on 16/4/26.
 */
public class pageServelt extends HttpServlet
{
    Connection connection=null;
    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        String dbURL = "jdbc:mysql://127.0.0.1:3306/pagediease?user=root&password=root";

        Properties props = new Properties();

        props.setProperty("characterEncoding", "gbk");

        try {
            connection = DriverManager.getConnection(dbURL, props);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       // super.doPost(req, resp);

        System.out.println("getpage");
        req.setCharacterEncoding("gbk");
       // String name=req.getParameter("name");
        String category=req.getParameter("category");
      //  String sex=req.getParameter("sex");
      //  System.out.println(name);
        System.out.println("category="+category.length());
        try {
            Statement statement=connection.createStatement();
          //  String sql="select * from pagecontent  where category like '%"+category+"%' limit 10";
            String sql="select * from pagecontent where category like '%"+category+"%' limit 10";
            ResultSet resultSet=statement.executeQuery(sql);
            String res="[";
            JSONArray jsonArray=new JSONArray();
            int i=0;
            while (resultSet.next())
            {
              //  element=element+"\"title\":\""+resultSet.getString("title")+"\",";
              //  element=element+"\"content\":\""+new String(resultSet.getBytes("content"))+"\"}";
              //  element=element+"\"content\":\"fasfd\"}";
              //  res=res+element+",";
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("_id",resultSet.getString("_id"));
                jsonObject.put("title",resultSet.getString("title"));
                jsonObject.put("content",new String(resultSet.getBytes("content")));
                jsonArray.add(i, jsonObject);
                i++;
            }
            res=res.substring(0,res.length()-1)+"]";
            res=jsonArray.toString();
           System.out.println("res="+res);
          //  res="孟凡山";
            //  res="[{\"title\":\"小儿先天性心脏病的科普知识_家庭医生在线育儿频道\",\"content\":fasfd},{\"title\":\"新疆儿童患上罕见先心病 先天性心脏病是怎么导致的_家庭医生在线育儿频道\",\"content\":fasfd}]";
            res="\r\n"+res+"\r\n";

           // res="\r\n"+"[{\"title\":fafaf,\"content\":fafaf},{\"title\":fasfd,\"content\":afadsf}]"+"\r\n";
            OutputStream outputStream=resp.getOutputStream();
            byte[] bytes=res.getBytes("gbk");
            resp.setHeader("Content-Length", bytes.length + "");
            for(int j=0;j<bytes.length;j++)
            {
                //System.out.println(bytes[j]+"   ");
            }
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
