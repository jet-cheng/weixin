package com.wini.weixin;

import java.io.File;
import java.io.IOException;

import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.renderer.freemarker.AbstractFreeMarkerRenderer;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FrontRenderer extends AbstractFreeMarkerRenderer {
	
	private static final Logger LOGGER = Logger.getLogger(FrontRenderer.class.getName());
	public static final Configuration TEMPLATE_CFG;
	
	static{
		TEMPLATE_CFG = new Configuration();
		TEMPLATE_CFG.setDefaultEncoding("UTF-8");
        try {
            final String webRootPath = WeixinServletListener.getWebRoot();
            TEMPLATE_CFG.setDirectoryForTemplateLoading(new File(webRootPath));
        } catch (final IOException e) {
            LOGGER.log(Level.ERROR,"初始化路径模板路径失败",e);
        }
	}
	

	@Override
	protected Template getTemplate(String templateDirName, String templateName) throws IOException {
		return TEMPLATE_CFG.getTemplate(templateName);
	}

	@Override
	protected void beforeRender(HTTPRequestContext context) throws Exception {

	}

	@Override
	protected void afterRender(HTTPRequestContext context) throws Exception {

	}

}
