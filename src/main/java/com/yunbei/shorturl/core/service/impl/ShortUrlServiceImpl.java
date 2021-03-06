package com.yunbei.shorturl.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunbei.shorturl.core.base.dto.ProcessStatus;
import com.yunbei.shorturl.core.base.enums.ErrorCode;
import com.yunbei.shorturl.core.base.utils.FeistelUtil;
import com.yunbei.shorturl.core.cache.RedisCache;
import com.yunbei.shorturl.core.dao.ShortUrlDao;
import com.yunbei.shorturl.core.entity.ShortUrl;
import com.yunbei.shorturl.core.enums.AccountSource;
import com.yunbei.shorturl.core.service.IShortUrlService;

@Service
public class ShortUrlServiceImpl implements IShortUrlService {

    private static final Logger LOG = LoggerFactory.getLogger(ShortUrlServiceImpl.class);

    @Autowired
    private ShortUrlDao shortUrlDao;

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
            result.setMessage(ErrorCode.URL_IS_NULL.getMsg());
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
        result.setMessage(ErrorCode.CONVERT_SHORT_URL_ERROR.getMsg());
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

            if (shortUrl != null) {
                redisCache.setEx(key, shortUrl, 60 * 60);
                redisCache.setEx(shortUrl.getShortUrlIndex(), shortUrl.getUrl(), 3 * 60 * 60);
            }
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
                redisCache.setEx(key, shortUrl, 60 * 60);
                redisCache.setEx(shortUrl.getShortUrlIndex(), shortUrl.getUrl(), 3 * 60 * 60);
                return result;
            }
        }
        return result;
    }

    @Override
    public ProcessStatus convert2Long(String shortUrlIndex) {

        ProcessStatus result = new ProcessStatus(false);

        String url = redisCache.get(shortUrlIndex, String.class);
        if (StringUtils.isBlank(url)) {
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("shortUrlIndex", shortUrlIndex);
            ShortUrl shortUrl = shortUrlDao.selectByAccount(map);
            if (shortUrl != null) {
                url = shortUrl.getUrl();
                redisCache.setEx(shortUrl.getShortUrlIndex(), shortUrl.getUrl(), 3 * 60 * 60);
            }
        }
        if (StringUtils.isNotBlank(url)) {
            result.setSuccess(true);
            result.setResult(url);
            return result;
        }

        result.setMessage("转换失败");
        return result;
    }

}
