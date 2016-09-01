package com.yunbei.shorturl.core.dao;

import com.yunbei.shorturl.core.entity.VisitorLog;

public interface VisitorLogDao {

    public long insert(VisitorLog visitorLog);

    // public long insertBatch(List<VisitorLog> visitorLogs);

}
