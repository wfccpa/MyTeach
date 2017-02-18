package one.ui;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import one.jssdk.HttpClientUtil;

/**
 * Servlet implementation class ReplyServlet
 */
@WebServlet("/r")
public class ReplyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReplyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	//做个主动发送消息的实验,实际上是不成功的，无法得知客户端的url
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String xml="<xml><ToUserName><![CDATA[oNo3WwySr9EffpZoxKLz0Er4sWj4]]></ToUserName><FromUserName><![CDATA[gh_28b359576381]]></FromUserName><CreateTime>1486347393065</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[嗯:1486347392]]></Content></xml>";
		// HttpClientUtil.getInstance().sendHttpPost(httpUrl, xml);//url你写不下去，就像服务器无法主动推送给请求方一样，所以被动消息是要即时答复的
		 System.out.println("sendOk");
		 System.out.println("say hello");
		 System.out.println("Today is good day");
		 System.out.println("服务器端修改");
 }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
