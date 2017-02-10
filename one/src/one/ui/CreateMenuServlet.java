package one.ui;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import one.jssdk.HttpClientUtil;
import one.jssdk.WeiXinUtil;

/**
 * Servlet implementation class CreateMenuServlet
 */
@WebServlet(name = "MenuServlet", urlPatterns = { "/menu" })
public class CreateMenuServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log=Logger.getLogger(CreateMenuServlet.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateMenuServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/menu.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String opr=request.getParameter("opr");
		String menujson=request.getParameter("menujson");
		String token=WeiXinUtil.getAccessTocken();//"YrYc1p9iOFwfZz581XSZaWte8njtH7V72EFUZf6GBD-gVMBODoUSO396y-7mB8hrhe9gbfQ3fhZ-30MrI3jPWCf-n67ekkzvLW_n5z4lU5XDZZSFqbckB7sJtLBPBAmnBAUfADAONH";
		//https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
		switch(opr)
		{
		  case "create":
			  String url="https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+token;
			  log.info(HttpClientUtil.getInstance().sendHttpPost(url,menujson));
			  break;
		  case "del":break;
		}
		
	}
//	public static String sendGet(String url) throws IOException
//	{
//		HttpClient client = new HttpClient();//����һ���൱��������ͻ���
//		GetMethod get = new GetMethod(url);//����һ��post�ύ������ 
//		
//		client.executeMethod(get);//����
//		
//		//����״̬ 200 404
//		int statusCode = get.getStatusCode();
//		System.out.println("statusCode:"+statusCode);
//		
//		//������Ϣ��ֵ������ĺ���鿴api�ĵ�
//		String result =get.getResponseBodyAsString();
//		get.releaseConnection();//�����Ѿ���ɣ��ͷ�����
//        return result;
//	}
//	public static String sendPost(String url,String postdata) throws HttpException, IOException
//	{
//		
//		HttpClient client = new HttpClient();//����һ���൱��������ͻ���
//		PostMethod post = new PostMethod(url);//����һ��post�ύ������ 
//		post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");//˵������ʽ
//		RequestEntity en=new ByteArrayRequestEntity(postdata.getBytes("UTF-8"));
//		post.setRequestEntity(en);
//
//		client.executeMethod(post);//����
//		
//		//����״̬ 200 404
//		int statusCode = post.getStatusCode();
//		System.out.println("statusCode:"+statusCode);
//		
//		//������Ϣ��ֵ������ĺ���鿴api�ĵ�
//		String result =post.getResponseBodyAsString();
//		post.releaseConnection();//�����Ѿ���ɣ��ͷ�����
//        return result;
//	}

}
