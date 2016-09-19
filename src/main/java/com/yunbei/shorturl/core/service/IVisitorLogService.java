package com.yunbei.shorturl.core.service;

import com.yunbei.shorturl.core.entity.VisitorLog;

public interface IVisitorLogService {

    public long add(VisitorLog visitorLog);

    public long countByIndex(String shortUrlIndex, Long beginTs, Long endTs);

    public long countVisitorsByIndex(String shortUrlIndex, Long beginTs, Long endTs);

}
