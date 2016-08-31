package com.yunbei.shorturl.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yunbei.shorturl.core.base.controller.BaseController;
import com.yunbei.shorturl.core.base.dto.BaseResult;
import com.yunbei.shorturl.core.base.dto.ProcessStatus;
import com.yunbei.shorturl.core.base.enums.ErrorCode;
import com.yunbei.shorturl.core.shorturl.entity.ShortUrl;
import com.yunbei.shorturl.core.shorturl.service.IShortUrlService;

@Controller
public class ShortUrlCtrl extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(ShortUrlCtrl.class);

    @Autowired
    private IShortUrlService shortUrlService;

    /**
     * 获取短链接
     */
    @RequestMapping(value = "/short", method = RequestMethod.GET)
    public BaseResult getShortUrl(String account, Integer accountSource, String url) {

        ProcessStatus processStatus = shortUrlService.convert2Short(account, accountSource, url);

        LOG.warn("processStatus: ", processStatus.toString());
        if (processStatus.isSuccess()) {

            ShortUrl shortUrl = (ShortUrl) processStatus.getResult();
            return successResult(shortUrl.getShortUrlIndex());
        } else {
            return failedResult(ErrorCode.CONVERT_SHORT_URL_ERROR.getCode(), processStatus.getMessage());
        }
    }

    @RequestMapping(value = "/{shortUrl}", method = RequestMethod.GET)
    public BaseResult getLongUrl(@PathVariable String shortUrl) {

        ProcessStatus processStatus = shortUrlService.convert2Long(shortUrl);

        LOG.warn("processStatus: ", processStatus.toString());

        if (processStatus.isSuccess()) {
            return successResult(processStatus.getResult());
        } else {
            return failedResult(ErrorCode.CONVERT_SHORT_URL_ERROR.getCode(), processStatus.getMessage());
        }
    }
}
