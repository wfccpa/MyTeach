package test;

import java.io.StringReader;
import java.util.Date;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class Textxpath {

	public static void main(String[] args) {
		String xml="<xml><ToUserName><![CDATA[toUser]]></ToUserName><FromUserName><![CDATA[fromUser]]></FromUserName> <CreateTime>1348831860</CreateTime> <MsgType><![CDATA[text]]></MsgType><Content><![CDATA[this is a test]]></Content><MsgId>1234567890123456</MsgId></xml>";
		SAXReader sax = new SAXReader();
		Document doc = null;
		try {
			doc = sax.read(new StringReader(xml));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String to = doc.selectSingleNode("/xml/ToUserName").getText();
		String from = doc.selectSingleNode("/xml/FromUserName").getText();
		String cxt = doc.selectSingleNode("/xml/Content").getText();
		String time = new Date(Long.parseLong(doc.selectSingleNode("/xml/CreateTime").getText())).toString();
		String answer = cxt + ":" + time;
		System.out.println(to+" "+from+" "+cxt+" "+time);
	}

}
