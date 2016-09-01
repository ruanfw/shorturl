package com.yunbei.shorturl.web;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yunbei.shorturl.core.base.controller.BaseController;
import com.yunbei.shorturl.core.base.dto.BaseResult;
import com.yunbei.shorturl.core.base.dto.ProcessStatus;
import com.yunbei.shorturl.core.base.enums.ErrorCode;
import com.yunbei.shorturl.core.entity.ShortUrl;
import com.yunbei.shorturl.core.event.Event;
import com.yunbei.shorturl.core.event.EventProducer;
import com.yunbei.shorturl.core.event.EventType;
import com.yunbei.shorturl.core.service.IShortUrlService;

@Controller
@RestController
public class ShortUrlCtrl extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(ShortUrlCtrl.class);

    @Autowired
    private IShortUrlService shortUrlService;

    @Autowired
    private EventProducer eventProducer;

    /**
     * 获取短链接
     */

    @RequestMapping(value = "/short", method = RequestMethod.GET)
    public BaseResult getShortUrl(String account, Integer accountSource, String url) {

        ProcessStatus processStatus = shortUrlService.convert2Short(account, accountSource, url);

        if (processStatus.isSuccess()) {

            ShortUrl shortUrl = (ShortUrl) processStatus.getResult();
            return successResult(shortUrl.getShortUrlIndex());
        } else {
            return failedResult(ErrorCode.CONVERT_SHORT_URL_ERROR.getCode(), processStatus.getMessage());
        }
    }

    /**
     * 短链接访问
     * 
     * @param shortUrl
     * @return
     */
    @RequestMapping(value = "/{shortUrl}", method = RequestMethod.GET)
    public void getLongUrl(@PathVariable String shortUrl) {

        ProcessStatus processStatus = shortUrlService.convert2Long(shortUrl);
        LOG.warn("----------------------ip:{}", getRequestIP());

        if (processStatus.isSuccess()) {

            String longUrl = (String) processStatus.getResult();
            if (!longUrl.startsWith("http://") && !longUrl.startsWith("https://")) {
                longUrl = "http://" + longUrl;
            }
            try {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("visitorTime", System.currentTimeMillis());
                params.put("ip", getRequestIP());
                params.put("shortUrlIndex", shortUrl);
                params.put("realUrl", longUrl);
                eventProducer.submit(new Event(EventType.VISITOR_LOG).setParams(params));
                response.sendRedirect(longUrl);
            } catch (Exception e) {
                return;
            }
        } else {
            return;
        }
    }
}
