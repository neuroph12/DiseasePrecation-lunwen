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
public class userprofileSevelt extends HttpServlet
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

        System.out.println("getuserprofile");
        req.setCharacterEncoding("gbk");
        try {
            Statement statement=connection.createStatement();
            String sql="SELECT * FROM userprofile;";
            ResultSet resultSet=statement.executeQuery(sql);
            JSONArray jsonArray=new JSONArray();
            int i=0;
            while (resultSet.next())
            {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("predicttime",resultSet.getString("predicttime"));
                jsonObject.put("name",resultSet.getString("name"));
                jsonObject.put("age",resultSet.getString("age"));
                jsonObject.put("sex",resultSet.getString("sex"));
                jsonObject.put("cp",resultSet.getString("cp"));
                jsonObject.put("trestbps",resultSet.getString("trestbps"));
                jsonObject.put("chol",resultSet.getString("chol"));
                jsonObject.put("fbs",resultSet.getString("fbs"));
                jsonObject.put("restecg",resultSet.getString("restecg"));
                jsonObject.put("thalach",resultSet.getString("thalach"));
                jsonObject.put("exang",resultSet.getString("exang"));
                jsonObject.put("oldpeak",resultSet.getString("oldpeak"));
                jsonObject.put("slope",resultSet.getString("slope"));
                jsonObject.put("ca",resultSet.getString("ca"));
                jsonObject.put("thal",resultSet.getString("thal"));
                jsonObject.put("result",resultSet.getString("result"));
                jsonArray.add(i, jsonObject);
                i++;
            }
            String res=jsonArray.toString();
            System.out.println("res="+res);
            res="\r\n"+res+"\r\n";
            OutputStream outputStream=resp.getOutputStream();
            byte[] bytes=res.getBytes("gbk");
            resp.setHeader("Content-Length", bytes.length + "");
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
