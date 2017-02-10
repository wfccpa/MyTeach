package one.jssdk;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import sun.misc.BASE64Encoder;

public class WeiXinUtil {
	//oNo3WwySr9EffpZoxKLz0Er4sWj4 测试客户openid
	public static final String MYWEIXIN="gh_28b3595763181";//gh_28b359576381
	public static final String APPID="wx2d5049909a02811f5";
	public static final String APPSECRET="e499f207a94c11e4e3b99cefb2c78d61";
	
	

	
	
	public static HttpClientUtil http=HttpClientUtil.getInstance();
	public static String accessTocken;//记录临时票证
	public static long tocken_lasttime=0;//临时票证的获取时间(毫秒数)
	public static String getAccessTocken() throws IOException
	{
		long now=new Date().getTime();
		if((now-tocken_lasttime)<7200000)//判断当前与上次获取时间是否超过2小时
		  return accessTocken; //还没超过返回旧的即可
		
		tocken_lasttime=now;
		String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",APPID,APPSECRET);
		String json=http.sendHttpGet(url);//利用HttpClient组件发起get请求
		JsonParser parse =new JsonParser();//返回结果是json格式,需要解析
		JsonObject jo=(JsonObject) parse.parse(json);
		accessTocken= jo.get("access_token").getAsString();//把临时票证提取出来,存在static中重用
		return accessTocken;
		
	}
	
	public static String jsapi_ticket;
	public static long jsapi_lasttime=0;
	 public static  String getJsApiTicket() throws IOException {
		 long now=new Date().getTime();
			if((now-jsapi_lasttime)<7200000)
			  return jsapi_ticket; 
		 jsapi_lasttime=now;
		 String url=String.format("https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=%s",getAccessTocken());
		 String json=http.sendHttpGet(url);
			JsonParser parse =new JsonParser();
			JsonObject jo=(JsonObject) parse.parse(json);
			jsapi_ticket= jo.get("ticket").getAsString();
			return jsapi_ticket;
	 }
	 public static Map<String,String> getSign(String url) throws IOException
	 {
		 return Sign.sign(getJsApiTicket(), url);
	 }
	//************************************************************** 
	public static String createUrl(String redirectUrl,boolean isfullinfo) throws UnsupportedEncodingException
	{
		return String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=STATE#wechat_redirect"
				,APPID,URLEncoder.encode(redirectUrl, "utf-8"),isfullinfo?"snsapi_userinfo":"snsapi_base"
				);
	}
	public static JsonObject getCode2AccessTocken(String code) throws IOException
	{
		String url=String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",APPID,APPSECRET,code);
		 String json=http.sendHttpGet(url);
		JsonParser parse =new JsonParser();
		JsonObject jo=(JsonObject) parse.parse(json);
		return jo;
	}
	
	public static JsonObject getUserInfo(String pageAccessTocken,String openId) throws IOException
	{
		String url=String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN",pageAccessTocken,openId);
		String json=http.sendHttpGet(url);
		JsonParser parse =new JsonParser();
		JsonObject jo=(JsonObject) parse.parse(json);
		return jo;
	}
	
	public static JsonObject getUserInfo(String code) throws IOException
	{
		JsonObject jo=getCode2AccessTocken(code);
		return getUserInfo(jo.get("access_token").getAsString(),jo.get("openid").getAsString());
	}
	
	//*************************************************************
	public static String uploadMedia(String mediaType,String filePath) throws IOException
	{
		return uploadMedia(mediaType,new File(filePath));
	}
	public static String uploadMedia(String mediaType,File file) throws IOException
	{
		String url=String.format("http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=%s&type=%s", getAccessTocken(),mediaType);
		String json= http.sendHttpPost(url,file);
		JsonParser parse =new JsonParser();
		JsonObject jo=(JsonObject) parse.parse(json);
		return jo.get("media_id").getAsString();
	}
	public static void downloadMedia(String mediaId,String savefilePath) throws IOException
	{
		String url=String.format("https://api.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s", getAccessTocken(),mediaId);
		http.downloadFile(url,savefilePath);
		
	}
	//**************************************************************	
	
	public static String createKfAccount(String uname,String pwd,String nickName) throws IOException
	{
		String url=String.format("https://api.weixin.qq.com/customservice/kfaccount/add?access_token=%s",getAccessTocken());
		String json=String.format("{'kf_account':'%s@%s','nickname':'%s'}",uname,MYWEIXIN,nickName).replace("'", "\"");
		System.out.println(json);
		String r= http.sendHttpPost(url, json);
		return r;
	}
	
	public static String kfSendText(String openId,String content) throws IOException
	{
		String url=String.format("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s",getAccessTocken());
		String json=String.format("{'touser':'%s','msgtype':'text','text':{'content':'%s'}}",openId,content).replace("'", "\"");
		String r= http.sendHttpPost(url, json);
		System.out.println(r);
		return r;
	}
	//**************************************************************
	 public static String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
	        //确定计算方法
	        MessageDigest md5=MessageDigest.getInstance("MD5");
	        BASE64Encoder base64en = new BASE64Encoder();
	        //加密后的字符串
	        String newstr=base64en.encode(md5.digest(str.getBytes("utf-8")));
	        return newstr;
	    }
	 private static String MD5_32(String sourceStr) throws UnsupportedEncodingException {
		    String result = "";
		    try {
		      MessageDigest md = MessageDigest.getInstance("MD5");
		      md.reset();    
		      md.update(sourceStr.getBytes("UTF-8"));    
		      byte b[] = md.digest();
		      int i;
		      StringBuffer buf = new StringBuffer("");
		      for (int offset = 0; offset < b.length; offset++) {
		        i = b[offset];
		        if (i < 0)
		          i += 256;
		        if (i < 16)
		          buf.append("0");
		        buf.append(Integer.toHexString(i));
		      }
		      result = buf.toString();
		    } catch (NoSuchAlgorithmException e) {
		      System.out.println(e);
		    }
		    return result;
		  }
//	 private static String getMD5Str(String str)    
//	    {    
//	        MessageDigest messageDigest = null;    
//	        try    
//	        {    
//	            messageDigest = MessageDigest.getInstance("MD5");    
//	            messageDigest.reset();    
//	            messageDigest.update(str.getBytes("UTF-8"));    
//	        } catch (NoSuchAlgorithmException e)    
//	        {    
//	            System.out.println("NoSuchAlgorithmException caught!");    
//	            System.exit(-1);    
//	        } catch (UnsupportedEncodingException e)    
//	        {    
//	            e.printStackTrace();    
//	        }    
//	    
//	        byte[] byteArray = messageDigest.digest();    
//	    
//	        StringBuffer md5StrBuff = new StringBuffer();    
//	    
//	        for (int i = 0; i < byteArray.length; i++)    
//	        {    
//	            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)    
//	                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));    
//	            else    
//	                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));    
//	        }    
//	        return md5StrBuff.toString();    
//	    }    
//	public static String sendGet(String url) throws IOException
//	{
//		CloseableHttpClient httpclient = HttpClients.createDefault();
//		HttpGet get=new HttpGet(url);
//		CloseableHttpResponse r=httpclient.execute(get);
//		
//		String content = EntityUtils.toString(r.getEntity(), "utf-8");
//		EntityUtils.consume(r.getEntity());
//		r.close();
//		httpclient.close();
//        return content;
//	}
//	public static String sendPost(String url,String postdata) throws IOException
//	{
//		CloseableHttpClient httpclient = HttpClients.createDefault();
//		HttpPost post=new HttpPost(url);
//		post.setEntity(new StringEntity(postdata,"UTF-8"));
//		CloseableHttpResponse r=httpclient.execute(post);
//		String content = EntityUtils.toString(r.getEntity(), "utf-8");
//		EntityUtils.consume(r.getEntity());
//		r.close();
//		httpclient.close();
//        return content;
//		
//	}
	 
	 public static String getRequestFullUrl(HttpServletRequest request)
		{
		   String q=request.getQueryString();
		   return request.getRequestURL()+(q==null?"":("?"+request.getQueryString()));
		}
	public static void main(String[] args) throws IOException, Exception
	{
	   //System.out.println(createUrl("http://wufan168.duapp.com/register",true));
		//String mid=uploadMedia("image","F:\\a.jpg");
		//System.out.println(mid);//tSweY8iRD1JwuH70W94UXBGPryZMbY2ikah6eA_eUFxU1CmUlVvADrLPGlkaT0j1
		//System.out.println(kfSendText("oNo3WwySr9EffpZoxKLz0Er4sWj4","您的消息我们已经收到"));
		
		//System.out.println(MD5_32("中国"));
		//System.out.println(EncoderByMd5("中国"));
		//System.out.println(createKfAccount("emp1","abc","小米"));
		
//		Date d1=new Date();
//		Thread.sleep(5000);
//		Date d2=new Date();
//		System.out.println(d2.getTime()-d1.getTime());
		
	}
}

