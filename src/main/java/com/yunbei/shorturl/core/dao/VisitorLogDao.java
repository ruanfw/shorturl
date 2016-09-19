package com.yunbei.shorturl.core.dao;

import java.util.Map;

import com.yunbei.shorturl.core.entity.VisitorLog;

public interface VisitorLogDao {

    public long insert(VisitorLog visitorLog);

    public long countByIndex(Map<String, Object> params);

    public long countVisitorsByIndex(Map<String, Object> params);

    // public long insertBatch(List<VisitorLog> visitorLogs);

}
