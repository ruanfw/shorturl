package com.yunbei.shorturl.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yunbei.shorturl.core.base.constant.ShortUrlConfigs;
import com.yunbei.shorturl.core.base.controller.BaseController;
import com.yunbei.shorturl.core.base.dto.BaseResult;
import com.yunbei.shorturl.core.base.enums.ErrorCode;
import com.yunbei.shorturl.core.dto.VisitorCountDto;
import com.yunbei.shorturl.core.service.IVisitorLogService;

@RestController
@RequestMapping("/visitor")
public class VisitorCtrl extends BaseController {

    @Autowired
    private IVisitorLogService visitorLogService;

    @RequestMapping(value = "/getVisitorCount", method = RequestMethod.GET)
    public BaseResult getVisitorCount(String shortUrlIndex, Long beginTs, Long endTs) {

        if (StringUtils.isBlank(shortUrlIndex)) {
            return failedResult(ErrorCode.URL_IS_NULL.getCode(), "请输入短链接");
        }

        if (shortUrlIndex.startsWith(ShortUrlConfigs.HOST)) {
            shortUrlIndex = shortUrlIndex.replace(ShortUrlConfigs.HOST, "");
        }

        long pv = visitorLogService.countByIndex(shortUrlIndex, beginTs, endTs);

        long uv = visitorLogService.countVisitorsByIndex(shortUrlIndex, beginTs, endTs);

        VisitorCountDto visitorCountDto = new VisitorCountDto().setPv(pv).setUv(uv);

        return successResult(visitorCountDto);
    }

}
