package one.ui;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.util.*;
import java.util.logging.Logger;

/**
 * Servlet implementation class StartWeiXinServlet
 */
@WebServlet("/start")
public class StartWeiXinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(StartWeiXinServlet.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StartWeiXinServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("接入验证");
		response.getWriter().print(request.getParameter("echostr"));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/xml");
		InputStream is = request.getInputStream();
		byte[] ary = new byte[10000];
		int len = is.read(ary);
		log.info(ary.length + "");
		log.info(len + "");
		String xml = new String(ary, 0, len, "UTF-8");

		log.info(xml);
		SAXReader sax = new SAXReader();
		Document doc = null;
		try {
			doc = sax.read(new StringReader(xml));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Element root = doc.getRootElement();
		Map<String, String> map = new HashMap<String, String>();
		for (Object n : root.elements())
			map.put(((Element) n).getName(), ((Element) n).getText());
		String opr = map.get("MsgType");
		String anXml = "";
		switch (opr) {
		case "text":
			String cnt = map.get("Content");
			if (cnt.indexOf("图片") != -1)
				anXml = oprImg(map);
			else
				anXml = oprText(map);
			break;
		}
		response.getWriter().print(anXml);
	}

	private String oprText(Map<String, String> map) {
		String to = map.get("ToUserName");
		String from = map.get("FromUserName");
		String cxt = map.get("Content");
		String time = map.get("CreateTime");
		String answer = cxt + ":" + time;

		// 回复
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		sb.append("<ToUserName><![CDATA[@to]]></ToUserName>");
		sb.append("<FromUserName><![CDATA[@from]]></FromUserName>");
		sb.append("<CreateTime>@time</CreateTime>");
		sb.append("<MsgType><![CDATA[text]]></MsgType>");
		sb.append("<Content><![CDATA[@cxt]]></Content>");
		sb.append("</xml>");
		String anXml = sb.toString().replaceAll("@to", from).replaceAll("@from", to)
				.replaceAll("@time", new Date().getTime() + "").replaceAll("@cxt", answer);
		System.out.println("out输出:"+anXml);
		log.info(anXml);
		return anXml;

	}

	private String oprImg(Map<String, String> map) {
		String to = map.get("ToUserName");
		String from = map.get("FromUserName");
		String cxt = map.get("Content");
		String time = map.get("CreateTime");
		String imgid = "tSweY8iRD1JwuH70W94UXBGPryZMbY2ikah6eA_eUFxU1CmUlVvADrLPGlkaT0j1";

		// 回复
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		sb.append("<ToUserName><![CDATA[@to]]></ToUserName>");
		sb.append("<FromUserName><![CDATA[@from]]></FromUserName>");
		sb.append("<CreateTime>@time</CreateTime>");
		sb.append("<MsgType><![CDATA[image]]></MsgType>");
		sb.append("<Image>");
		sb.append("<MediaId><![CDATA[@imgid]]></MediaId>");
		sb.append("</Image>");
		sb.append("</xml>");
		String anXml = sb.toString().replaceAll("@to", from).replaceAll("@from", to)
				.replaceAll("@time", new Date().getTime() + "").replaceAll("@imgid", imgid);
		log.info(anXml);
		return anXml;

	}

}
