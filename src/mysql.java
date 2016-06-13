import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by novas on 16/4/24.
 */
public class mysql {
    private static mysql mysql = null;
    public Connection connection;
    private mysql() {

    }
    public int createTableOrInsert(String sql)
    {
        java.sql.Statement stmt=null;
        try {
            stmt  = connection.createStatement();
            stmt.execute(sql);
            return 1;
        }
        catch (SQLException e)
        {
            return -1;
        }

    }
    public Connection getConnection()throws SQLException
    {
        String dbURL = "jdbc:mysql://127.0.0.1:3306/pagediease?user=root&password=root";

        Properties props = new Properties();

        props.setProperty("characterEncoding", "utf-8");

        connection = DriverManager.getConnection(dbURL, props);
        return connection;
    }
    public static mysql getMySqlInstance() {
        if (mysql == null) {
            mysql = new mysql();
            return mysql;
        }
        return mysql;
    }

    public void init(){
        try{
            //加载MySql的驱动类
            Class.forName("com.mysql.jdbc.Driver") ;

        }catch(ClassNotFoundException e){
            System.out.println("找不到驱动程序类，加载驱动失败！");
            e.printStackTrace() ;
        }
    }
}
