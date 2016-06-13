
import com.oracle.javafx.jmx.json.JSONDocument;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Iterator;
import java.util.List;

public class HelloWorld extends HttpServlet {

    private String message;
    private XorExample xorExample;
    @Override
    public void init() throws ServletException {
        System.out.println("in init");
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       // resp.setContentType("text/html");
       // resp.setHeader("content-type", "text/html;charset=gbk");

       // resp.setCharacterEncoding("gbk");
        req.setCharacterEncoding("gbk");
        //   BufferedReader br=req.getReader();
      //  String line=null;
      //  while ((line=br.readLine())!=null)
      //  {
            //System.out.println(line);
      //  }
      //  PrintWriter out=null;
        OutputStream os=null;
        try {
           // out= resp.getWriter();
            os=resp.getOutputStream();
        }
        catch (IOException e)
        {

        }
        System.out.println(req.getParameter("uname"));

       // os.write(req.getParameter("uname").getBytes("gbk"));
        String demo=new String("萌翻山 ");
        String s="\r\n";
        for(int i=0;i<1000;i++)
        {
            s=s+demo;
        }
        s=s+"\r\n";
        //JSONObject
       // JSONDocument jsonDocument=new JSONDocument();
      //  System.out.println(s.getBytes("gbk").length);
        resp.setHeader("Content-Length", s.getBytes("gbk").length + "");
        byte[] bytes=s.getBytes("gbk");
      //  for(int i=0;i<bytes.length;i++)
      //  {
       //     System.out.println(bytes[i]);
       // }

        os.write(bytes);
        os.flush();
       // out.println(s);
        //  out.println("lt"+req.getHeaderNames().toString());
        os.close();
//out.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//设置响应内容类型

    }

    @Override

    public void destroy() {

        super.destroy();

    }

}