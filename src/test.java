import java.sql.*;
import java.util.Properties;

/**
 * Created by novas on 16/4/7.
 */
public class test {
    /*
    private static XorExample xorExample;
    public static void main(String[] args)throws Exception
    {
        xorExample=new XorExample();
      //  xorExample.run();
    }
    */
    public static void test4() throws SQLException{
        String dbURL = "jdbc:mysql://127.0.0.1:3306/pagediease?user=root&password=root";

        Properties props = new Properties();

        props.setProperty("characterEncoding", "utf-8");

        Connection con = DriverManager.getConnection(dbURL, props);

        Statement stmt = con.createStatement();

        stmt.execute("create table userprofile ( id INTEGER  PRIMARY  KEY ,username VARCHAR(20) )");
        System.out.println("drop done");


    }

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        try{
            //加载MySql的驱动类
            Class.forName("com.mysql.jdbc.Driver") ;

        }catch(ClassNotFoundException e){
            System.out.println("找不到驱动程序类，加载驱动失败！");
            e.printStackTrace() ;
        }
     //   XorExample  xorExample=new XorExample();
      //  xorExample.train();
        RecommandModel recommandModel=new RecommandModel();
        mysql ms= mysql.getMySqlInstance();
        ms.init();
        Connection connection=ms.getConnection();
        recommandModel.initStep1();
        recommandModel.initStep2();
        PreparedStatement preparedStatement=connection.prepareStatement("select * from pagecontent ");
        ResultSet resultSet=preparedStatement.executeQuery();
        Statement statement=connection.createStatement();
        while (resultSet.next())
        {
            String vector=resultSet.getString("vector");
            System.out.println("vector=" + vector);
            vector=vector.substring(1,vector.length()-1);
            String[] array=vector.split(",");
            StringBuilder stringBuilder=new StringBuilder();
            for(int j=0;j<array.length;j++)
            {
                array[j]=array[j].split("=")[0];
                stringBuilder.append(array[j]);
            }
            String[] res=recommandModel.getMostSimilarityTagsFromContent(array);
            int id=resultSet.getInt("_id");
            StringBuilder sb=new StringBuilder();
            for(int i=0;i<res.length;i++)
            {
                System.out.println(res[i]);
                sb.append(res[i]+" ");
            }
            statement.executeUpdate("update  pagecontent set category='"+sb.toString()+"' where _id="+id);
        }


        //recommandModel.maxSimilarity("男人","孕妇");
      //  0.8533487736292318 0.8442708692252827 0.8345051408494156 0.8438230615900022 0.8351259979087268
      //  0.2198920858675965 0.21113968807134323 0.256468499072187 0.26501449364724533 0.22828511153164063
      //  double[] test=new double[4];
       // test[0]=0.2198920858675965;
      //  test[1]=0.21113968807134323;
      //  test[2]=0.256468499072187;
      //  test[3]=0.26501449364724533;
     //   System.out.println(xorExample.startPredict("xor.snet", test, 163));
    }
}
