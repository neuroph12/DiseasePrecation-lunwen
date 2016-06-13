import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by novas on 16/4/17.
 */
public class predictSevelt extends HttpServlet
{
    private String message;
    private XorExample xorExample;
    mysql mysql=null;
    @Override
    public void init() throws ServletException {
        System.out.println("in init");
        xorExample=new XorExample();
        mysql= mysql.getMySqlInstance();
        mysql.init();
        try {
            mysql.getConnection();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      //  super.doPost(req, resp);
        String result=null;
        req.setCharacterEncoding("gbk");
        String name=req.getParameter("name");
        String age=req.getParameter("age");
        String sex=req.getParameter("sex");
        String cp=req.getParameter("cp");
        String trestbps=req.getParameter("trestbps");
        String chol=req.getParameter("chol");
        String fbs=req.getParameter("fbs");
        String restecg=req.getParameter("restecg");
        String thalach=req.getParameter("thalach");
        String exang=req.getParameter("exang");
        String oldpeak=req.getParameter("oldpeak");
        String slope=req.getParameter("slope");
        String ca=req.getParameter("ca");
        String thal=req.getParameter("thal");
        System.out.println("age="+age+" "+sex+"  "+cp+"  "+trestbps+"  "+chol+"  "+fbs+"  "+restecg+"  "+thalach);
        System.out.println("=="+exang+" "+oldpeak+"  "+slope+"  "+ca+"  "+thal);
        double[] aa=new double[13];
        /*
        aa[0]=0.38;
        aa[1]=0.0;
        aa[2]=0.1;
        aa[3]=0.12;
        aa[4]=0.231;
        aa[5]=0.0;
        aa[6]=0.0;
        aa[7]=0.182;
        aa[8]=1.0;
        aa[9]=0.38;
        aa[10]=0.2;
        aa[11]=0.0;
        aa[12]=0.7;
*/
        aa[0]=Double.valueOf(age);
        aa[1]=Double.valueOf(sex);
        aa[2]=Double.valueOf(cp);
        aa[3]=Double.valueOf(trestbps);
        aa[4]=Double.valueOf(chol);
        System.out.println("fbs="+fbs);
        aa[5]=Double.valueOf(fbs);
        aa[6]=Double.valueOf(restecg);
        aa[7]=Double.valueOf(thalach);
        aa[8]=Double.valueOf(exang);
        aa[9]=Double.valueOf(oldpeak);
        aa[10]=Double.valueOf(slope);
        aa[11]=Double.valueOf(ca);
        aa[12]=Double.valueOf(thal);
        String s=xorExample.run(aa,resp)+"";
        result=s;
        System.out.println("s="+s);
        s="\r\n"+s+"\r\n";
        try
        {
            OutputStream os=null;
            os=resp.getOutputStream();
            resp.setHeader("Content-Length", s.getBytes("gbk").length + "");
            byte[] bytes=s.getBytes("gbk");
            os.write(bytes);
            os.flush();
            os.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Calendar calendar=Calendar.getInstance();
        Date date=calendar.getTime();
        double time=date.getTime();
        String insertsql="insert into userprofile values ( "+time+","+"'"+name+"'";
        for(int i=0;i<aa.length;i++)
        {
            insertsql=insertsql+","+aa[i];
        }
        insertsql=insertsql+","+result+")";
        System.out.println("insertsql="+insertsql);
        mysql.createTableOrInsert(insertsql);
    }
}
