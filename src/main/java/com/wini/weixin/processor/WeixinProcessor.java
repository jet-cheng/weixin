package com.wini.weixin.processor;

import java.util.Map;
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

@RequestProcessor
public class WeixinProcessor {

	public static final Logger LOGGER = Logger.getLogger(WeixinProcessor.class.getName());

	@RequestProcessing(value = { "/\\d*", "" }, uriPatternsMode = URIPatternMode.REGEX, method = HTTPRequestMethod.GET)
	public void index(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		final AbstractFreeMarkerRenderer renderer = new FrontRenderer();
		context.setRenderer(renderer);
		renderer.setTemplateName("index.ftl");
		final Map<String, Object> dataModel = renderer.getDataModel();
		dataModel.put("message", "扫描二维码即可关注维尼小弟的博客，获取最新的博客内容");
	}

}
