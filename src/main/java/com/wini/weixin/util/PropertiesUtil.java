package com.wini.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;

	
public class PropertiesUtil {
	
	private static final Logger LOGGER = Logger.getLogger(PropertiesUtil.class.getName());
	private static Properties pro;
	
	static{
		try {
			InputStream is = PropertiesUtil.class.getClassLoader().getResourceAsStream("weixin.properties");
			pro = new Properties();
			pro.load(is);
		} catch (IOException e) {
			LOGGER.log(Level.ERROR, "获得资源文件失败", e);
		}
	}
	
	public static String get(String key) throws Exception{
		return pro.getProperty(key);
	}
	
}
