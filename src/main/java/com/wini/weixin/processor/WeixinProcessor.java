package com.wini.weixin.processor;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.URIPatternMode;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.freemarker.AbstractFreeMarkerRenderer;

import com.wini.weixin.FrontRenderer;
import com.wini.weixin.util.Sha1Util;

@RequestProcessor
public class WeixinProcessor {

	public static final Logger LOGGER = Logger.getLogger(WeixinProcessor.class.getName());

	@RequestProcessing(value = { "/\\d*", "/index" }, uriPatternsMode = URIPatternMode.REGEX, method = HTTPRequestMethod.GET)
	public void index(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		final AbstractFreeMarkerRenderer renderer = new FrontRenderer();
		context.setRenderer(renderer);
		renderer.setTemplateName("index.ftl");
		final Map<String, Object> dataModel = renderer.getDataModel();
		dataModel.put("message", "扫描二维码即可关注维尼小弟的博客，获取最新的博客内容");
	}

	
	@RequestProcessing(value = {"/weixin"} , method = HTTPRequestMethod.GET)
	public void get(final HTTPRequestContext context ,final HttpServletRequest request,final HttpServletResponse response) throws Exception{
		final AbstractFreeMarkerRenderer renderer = new FrontRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("weixin.ftl");
        try{
			LOGGER.log(Level.INFO,"正在签名公众平台");
			String signature = request.getParameter("signature");
			String timestamp = request.getParameter("timestamp");
			String nonce = request.getParameter("nonce");
			String echostr = request.getParameter("echostr");
			LOGGER.info("微信公众平台相关参数[signature:"+signature+";timestamp:"+timestamp+";nonce:"+nonce+";echostr:"+echostr+"]");
			String[] params = new String[]{timestamp,nonce,echostr};
			Arrays.sort(params);
			String sign = "";
			for(String param : params){
				sign = sign + param;
			}
			sign = Sha1Util.hex_sha1(sign);
			LOGGER.info("签名密码:" + sign);
			if(sign.equals(signature)){
		        final Map<String, Object> dataModel = renderer.getDataModel();
		        dataModel.put("echostr", nonce);
			}
        }catch(Exception e){
        	renderer.setTemplateName("error.ftl");
        	final Map<String, Object> dataModel = renderer.getDataModel();
		    dataModel.put("error", "公众平台签名出错");
        }
	}
	
	@RequestProcessing(value = {"/weixin"} , method = HTTPRequestMethod.POST)
	public void post(final HTTPRequestContext context ,final HttpServletRequest request,final HttpServletResponse response) throws Exception{
		
	}
}
