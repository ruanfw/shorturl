package com.yunbei.shorturl.core.shorturl.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunbei.shorturl.core.base.cache.RedisCache;
import com.yunbei.shorturl.core.base.dto.ProcessStatus;
import com.yunbei.shorturl.core.base.enums.ReturnCodeMsg;
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

        }

        return null;
    }

    @Override
    public ShortUrl findByAccount(String account, Integer accountSource, String url) {

        // 查询缓存
        String key = redisCache.generateKey(String.valueOf(accountSource), account, url);
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

    @Override
    public ProcessStatus convert2Long(String account, Integer accountSource, String shortUrl) {
        // TODO Auto-generated method stub
        return null;
    }

}
