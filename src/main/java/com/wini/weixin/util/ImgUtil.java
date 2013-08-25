package com.wini.weixin.util;

import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ImgUtil {
		
	private static final Logger LOGGER = Logger.getLogger(ImgUtil.class.getName());
	
	public static String parseImg(String htmlContent) throws Exception{
		String defaultImg = PropertiesUtil.get("defaultImg");
		try{
			Document doc = Jsoup.parse(htmlContent);
			Elements imgs = doc.select("img");
			if(imgs != null && imgs.size() > 0){
				Element img = imgs.get(0);
				defaultImg = img.attr("src");
			}
		}catch(Exception e){
			LOGGER.log(Level.ERROR, "解析文章图片地址出错", e);
		}
		return defaultImg;
	}
	
	public static void main(String[] args) throws Exception{
		String imgUtil = ImgUtil.parseImg(PropertiesUtil.get("testContent"));
		System.out.println(imgUtil);
	}
}
