package com.yunbei.shorturl.core.shorturl.dao;

import java.util.Map;

import com.yunbei.shorturl.core.shorturl.entity.ShortUrl;

public interface IShortUrlDao {

    public long insert(ShortUrl shortUrl);

    public ShortUrl selectByAccount(Map map);

    public long update(ShortUrl shortUrl);

}
