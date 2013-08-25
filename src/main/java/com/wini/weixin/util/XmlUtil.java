package com.wini.weixin.util;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;


public class XmlUtil {
	
	private static final Logger LOGGER = Logger.getLogger(XmlUtil.class.getName());
	
	public static Map<String, Object> parseToMap(String xmlStr) throws Exception{
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new StringReader(xmlStr));
			Element root = doc.getRootElement();
			map = parseToMap(root);
			return map;
		}catch(Exception e){
			LOGGER.log(Level.ERROR, "解析微信请求的xml失败", e);
		}
		return null;
	}
	
	
	private static Map<String,Object> parseToMap(Element root) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		List<Element> children = root.getChildren();
		if(children != null && children.size() > 0){
			for(Element child : children){
				if(child.getChildren() == null || child.getChildren().size() <= 0){
					map.put(child.getName(), child.getText());
				}else{
					Map<String,Object> value = parseToMap(child);
					map.put(child.getName(), value);
				}
			}
		}
		return map;
	}
	
	public static void main(String[] args){
		String s = " <xml> " +
				 "<ToUserName><![CDATA[toUser]]></ToUserName>" +
				 "<FromUserName><![CDATA[fromUser]]></FromUserName>" +
				 "<CreateTime>12345678</CreateTime>" +
				 "<MsgType><![CDATA[news]]></MsgType>" +
				 "<ArticleCount>2</ArticleCount>" +
				 "<Articles>" +
				 "<item>" +
				 "<Title><![CDATA[title1]]></Title> " +
				 "<Description><![CDATA[description1]]></Description>" +
				 "<PicUrl><![CDATA[picurl]]></PicUrl>" +
				 "<Url><![CDATA[url]]></Url>" +
				 "</item>" +
				 "<item>" +
				 "<Title><![CDATA[title]]></Title>" +
				 "<Description><![CDATA[description]]></Description>" +
				 "<PicUrl><![CDATA[picurl]]></PicUrl>" +
				 "<Url><![CDATA[url]]></Url>" +
				 "</item>" +
				 "</Articles>" +
				 "</xml>";
		try {
			XmlUtil.parseToMap(s);
			System.out.println(null instanceof String);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
}
