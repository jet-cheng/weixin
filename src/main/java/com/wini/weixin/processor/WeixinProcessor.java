package com.wini.weixin.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.b3log.latke.Latkes;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.URIPatternMode;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.freemarker.AbstractFreeMarkerRenderer;
import org.json.JSONObject;

import com.wini.weixin.FrontRenderer;
import com.wini.weixin.model.Article;
import com.wini.weixin.model.MsgType;
import com.wini.weixin.model.Weixin;
import com.wini.weixin.service.ArticleQueryService;
import com.wini.weixin.util.ImgUtil;
import com.wini.weixin.util.PropertiesUtil;
import com.wini.weixin.util.Sha1Util;
import com.wini.weixin.util.XmlUtil;

@RequestProcessor
public class WeixinProcessor {

	public static final Logger LOGGER = Logger.getLogger(WeixinProcessor.class.getName());

	@Inject
	private ArticleQueryService articleQueryService;

	public void setArticleQueryService(ArticleQueryService articleQueryService) {
		this.articleQueryService = articleQueryService;
	}

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
				LOGGER.log(Level.INFO, "签名成功");
				HttpServletResponse response = context.getResponse();
				response.getWriter().print(echostr);
				response.getWriter().close();
			}
		} catch (Exception e) {
			LOGGER.log(Level.ERROR, "签名失败", e);
		}
	}

	@RequestProcessing(value = { "/weixin" }, method = HTTPRequestMethod.POST)
	public void post(final HTTPRequestContext context) throws Exception {
		final AbstractFreeMarkerRenderer renderer = new FrontRenderer();
		context.setRenderer(renderer);
		renderer.setTemplateName("index.ftl");
		final Map<String, Object> dataModel = renderer.getDataModel();
		dataModel.put("message", "扫描二维码即可关注维尼小弟的博客，获取最新的博客内容");
		try{
			int articleCount = 5;
			List<JSONObject> articles = articleQueryService.getRecentArticles(articleCount);	
			
			HttpServletRequest request = context.getRequest();
			String postStr = readStreamParameter(request);
			LOGGER.info("POST的信息:[" + postStr + "]");
			Map<String, Object> map = XmlUtil.parseToMap(postStr);
			String fromUserName = (String)map.get(Weixin.TOUSERNAME);
			String toUserName = (String)map.get(Weixin.FROMUSERNAME);
			long createTime = (new Date()).getTime();
			String msgType = MsgType.NEWS;
			
			StringBuffer articlesXml = new StringBuffer();
			MessageFormat itemFormat = new MessageFormat(PropertiesUtil.get(Weixin.ITEM));
			for(JSONObject article : articles){
				String title = article.getString(Article.ARTICLE_TITLE);
				String description = article.getString(Article.ARTICLE_ABSTRACT);
				String picUrl = ImgUtil.parseImg(article.getString(Article.ARTICLE_CONTENT));
				String url = "http://www.winilog.com" + article.getString(Article.ARTICLE_PERMALINK);
				LOGGER.log(Level.INFO, "title:" + title);
				LOGGER.log(Level.INFO, "description:" + description);
				LOGGER.log(Level.INFO, "picUrl:" + picUrl);
				LOGGER.log(Level.INFO, "url:" + url);
				articlesXml.append(itemFormat.format(new String[]{title,description,picUrl,url}));
			}
			MessageFormat newsFormat = new MessageFormat(PropertiesUtil.get(MsgType.NEWS));
			String content = newsFormat.format(new Object[]{toUserName,fromUserName,createTime,msgType,articleCount,articlesXml});
			HttpServletResponse response = context.getResponse();
			print(response,content);
		}catch(Exception e){
			LOGGER.log(Level.ERROR, "请求失败", e);
		}
		
		
	}


	private String readStreamParameter(HttpServletRequest request) {
		try {
			InputStream in = request.getInputStream();
			StringBuilder buffer = new StringBuilder();
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
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
		} catch (Exception e1) {

		}
		return null;
	}

	private void print(HttpServletResponse response, String content) {
		try {
			response.setCharacterEncoding("utf-8");
			LOGGER.info("character:[utf-8]");
			response.getWriter().print(content);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {

		}
	}
	
	
	public static void main(String[] args) throws Exception{
		String s = PropertiesUtil.get(Weixin.ITEM);
		MessageFormat formate = new MessageFormat(s);
		System.out.println(formate.format(new String[]{"2","3","1"}));
	}
}
