package one.jssdk;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doget");
		String code=request.getParameter("code");
		if(code!=null)
		{
			JsonObject jo=WeiXinUtil.getUserInfo(code);
			request.setAttribute("nickname", jo.get("nickname").getAsString());
			request.setAttribute("img", jo.get("headimgurl").getAsString());
		}
		Map<String,String> map=WeiXinUtil.getSign(WeiXinUtil.getRequestFullUrl(request));
		request.setAttribute("appId", WeiXinUtil.APPID);
		request.setAttribute("map", map);
		request.getRequestDispatcher("/abc.jsp").forward(request, response);
	    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("dopost");
		doGet(request, response);
	}

}
