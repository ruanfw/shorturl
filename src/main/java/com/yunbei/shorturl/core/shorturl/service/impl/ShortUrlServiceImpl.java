package com.yunbei.shorturl.core.shorturl.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunbei.shorturl.core.base.cache.RedisCache;
import com.yunbei.shorturl.core.base.dto.ProcessStatus;
import com.yunbei.shorturl.core.base.enums.ReturnCodeMsg;
import com.yunbei.shorturl.core.base.utils.FeistelUtil;
import com.yunbei.shorturl.core.shorturl.dao.IShortUrlDao;
import com.yunbei.shorturl.core.shorturl.entity.ShortUrl;
import com.yunbei.shorturl.core.shorturl.enums.AccountSource;
import com.yunbei.shorturl.core.shorturl.service.IShortUrlService;

@Service
public class ShortUrlServiceImpl implements IShortUrlService {

	private static final Logger LOG = LoggerFactory.getLogger(ShortUrlServiceImpl.class);

	@Autowired
	private IShortUrlDao shortUrlDao;

	@Autowired
	private RedisCache redisCache;

	@Override
	public ProcessStatus convert2Short(String account, Integer accountSource, String url) {

		ProcessStatus result = new ProcessStatus(false);

		if (StringUtils.isBlank(account)) {
			account = "0";
		}
		if (accountSource == null) {
			accountSource = AccountSource.UNKNOW.getSource();
		}

		if (StringUtils.isBlank(url)) {
			result.setSuccess(false);
			result.setMessage(ReturnCodeMsg.URL_IS_NULL.getMsg());
			return result;
		}

		ShortUrl shortUrl = findByAccount(account, accountSource, url);

		if (shortUrl == null) {
			shortUrl = new ShortUrl(account, accountSource, url);
			long ret = addShortUrl(shortUrl);
			if (ret > 0) {
				result.setSuccess(true);
				result.setResult(shortUrl);
				return result;
			}
		} else {
			result.setSuccess(true);
			result.setResult(shortUrl);
			return result;
		}

		result.setSuccess(false);
		result.setMessage(ReturnCodeMsg.CONVERT_SHORT_URL_ERROR.getMsg());
		return result;
	}

	@Override
	public ShortUrl findByAccount(String account, Integer accountSource, String url) {

		// 查询缓存
		String key = redisCache.genKey(ShortUrl.class, String.valueOf(accountSource), account, url);
		ShortUrl shortUrl = redisCache.get(key, ShortUrl.class);

		if (shortUrl == null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("account", account);
			map.put("accountSource", accountSource);
			map.put("url", url);
			shortUrl = shortUrlDao.selectByAccount(map);
		}

		return shortUrl;
	}

	@Transactional
	@Override
	public long addShortUrl(ShortUrl shortUrl) {

		long result = 0;
		if (shortUrl == null) {
			return result;
		}

		result = shortUrlDao.insert(shortUrl);

		if (result > 0) {
			String shortUrlIndex = FeistelUtil.getIndex(shortUrl.getId());
			shortUrl.setShortUrlIndex(shortUrlIndex);
			result = shortUrlDao.update(shortUrl);
			if (result > 0) {
				String key = redisCache.genKey(shortUrl);
				redisCache.setEx(key, shortUrl, 60);
				return result;
			}
		}
		return result;
	}

	@Override
	public ProcessStatus convert2Long(String account, Integer accountSource, String shortUrl) {
		// TODO Auto-generated method stub
		return null;
	}

}
