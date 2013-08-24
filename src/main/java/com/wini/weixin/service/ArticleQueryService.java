package com.wini.weixin.service;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.repository.RepositoryException;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.service.annotation.Service;
import org.json.JSONObject;

import com.wini.weixin.repository.ArticleRepository;
import com.wini.weixin.repository.TagRepository;

@Service
public class ArticleQueryService {
	
	private static final Logger LOGGER = Logger.getLogger(ArticleQueryService.class.getName());
	
	@Inject
	private TagRepository tagRepository;
	@Inject
	private ArticleRepository articleRepository;

	public List<JSONObject> getAllTags() throws Exception {
		return this.tagRepository.getAllTags();
	}

	public List<JSONObject> getRecentArticles(final int fetchSize) throws ServiceException {
		try {
			return articleRepository.getRecentArticles(fetchSize);
		} catch (final RepositoryException e) {
			LOGGER.log(Level.ERROR, "Gets recent articles failed", e);
			return Collections.emptyList();
		}
	}
}
