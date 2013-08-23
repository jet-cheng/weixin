package com.wini.weixin.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.b3log.latke.Latkes;
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

	public static final Logger LOGGER = Logger.getLogger(WeixinProcessor.class
			.getName());

	@RequestProcessing(value = { "/\\d*", "/index" }, uriPatternsMode = URIPatternMode.REGEX, method = HTTPRequestMethod.GET)
	public void index(final HTTPRequestContext context) throws Exception {
		final AbstractFreeMarkerRenderer renderer = new FrontRenderer();
		context.setRenderer(renderer);
		renderer.setTemplateName("index.ftl");
		final Map<String, Object> dataModel = renderer.getDataModel();
		dataModel.put("message", "扫描二维码即可关注维尼小弟的博客，获取最新的博客内容");
	}

	@RequestProcessing(value = { "/weixin" }, method = HTTPRequestMethod.GET)
	public void get(final HTTPRequestContext context) throws Exception {
		final AbstractFreeMarkerRenderer renderer = new FrontRenderer();
		context.setRenderer(renderer);
		renderer.setTemplateName("weixin.ftl");
		try {
			LOGGER.log(Level.INFO, "正在签名公众平台");
			HttpServletRequest request = context.getRequest();
			String signature = request.getParameter("signature");
			String timestamp = request.getParameter("timestamp");
			String nonce = request.getParameter("nonce");
			String echostr = request.getParameter("echostr");
			String token = Latkes.getLocalProperty("weixin.token");
			LOGGER.info("微信公众平台相关参数[signature:" + signature + ";timestamp:"
					+ timestamp + ";nonce:" + nonce + ";echostr:" + echostr
					+ "]");
			String[] params = new String[] { token, timestamp, nonce };
			Arrays.sort(params);
			String sign = "";
			for (String param : params) {
				sign = sign + param;
			}
			sign = Sha1Util.Encrypt(sign);
			LOGGER.info("签名密码:" + sign);
			if (sign.equals(signature)) {
				LOGGER.info("签名成功");
				HttpServletResponse response = context.getResponse();
				response.getWriter().print(echostr);
				response.getWriter().close();
			}
		} catch (Exception e) {
			renderer.setTemplateName("error.ftl");
			final Map<String, Object> dataModel = renderer.getDataModel();
			dataModel.put("error", "公众平台签名出错");
		}
	}

	@RequestProcessing(value = { "/weixin" }, method = HTTPRequestMethod.POST)
	public void post(final HTTPRequestContext context) throws Exception {
		HttpServletRequest request = context.getRequest();
		String postStr = readStreamParameter(request.getInputStream());
		LOGGER.info("POST的信息:[" + postStr + "]");
	}

	// 从输入流读取post参数
	public String readStreamParameter(ServletInputStream in) {
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return buffer.toString();
	}
}
