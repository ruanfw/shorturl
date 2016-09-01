package com.yunbei.shorturl.core.event.handler.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunbei.shorturl.core.cache.parse.EveryParser;
import com.yunbei.shorturl.core.entity.VisitorLog;
import com.yunbei.shorturl.core.event.Event;
import com.yunbei.shorturl.core.event.EventType;
import com.yunbei.shorturl.core.event.handler.IEventHandler;
import com.yunbei.shorturl.core.service.IVisitorLogService;

@Component
public class VisitorLogHandler implements IEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(VisitorLogHandler.class);

    @Autowired
    private IVisitorLogService visitorLogService;

    @Override
    public void deal(Event event) {

        if (event == null) {
            LOG.warn("event is null");
            return;
        }

        Map<String, Object> params = event.getParams();
        if (params == null || params.size() <= 0) {
            LOG.warn("event params is empty");
            return;
        }

        VisitorLog visitorLog = EveryParser.map2Object(params, VisitorLog.class);

        long ret = visitorLogService.add(visitorLog);

        if (ret > 0) {
            LOG.warn("visitor log add to db success");
        } else {
            LOG.warn("visitor log add to db faild");
        }
    }

    @Override
    public List<EventType> getHandleEventTypes() {
        return Arrays.asList(EventType.VISITOR_LOG);
    }

}
