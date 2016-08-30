package com.yunbei.shorturl.core.shorturl.service;

import com.yunbei.shorturl.core.base.dto.ProcessStatus;
import com.yunbei.shorturl.core.shorturl.entity.ShortUrl;

public interface IShortUrlService {

	// public long add(ShortUrl shortUrl);
	//
	// public ShortUrl findByAccount(Map map);
	//
	// public long modify(ShortUrl shortUrl);

	public ProcessStatus convert2Short(String account, Integer accountSource, String url);

	public ProcessStatus convert2Long(String account, Integer accountSource, String shortUrl);

	public ShortUrl findByAccount(String account, Integer accountSource, String url);

	public long addShortUrl(ShortUrl shortUrl);

}
