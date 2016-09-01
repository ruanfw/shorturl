package com.yunbei.shorturl.core.dao;

import java.util.Map;

import com.yunbei.shorturl.core.entity.ShortUrl;

public interface ShortUrlDao {

    public long insert(ShortUrl shortUrl);

    public ShortUrl selectByAccount(Map map);

    public long update(ShortUrl shortUrl);

}
