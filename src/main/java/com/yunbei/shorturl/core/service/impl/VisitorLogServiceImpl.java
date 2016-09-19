package com.yunbei.shorturl.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunbei.shorturl.core.dao.VisitorLogDao;
import com.yunbei.shorturl.core.entity.VisitorLog;
import com.yunbei.shorturl.core.service.IVisitorLogService;

@Service
public class VisitorLogServiceImpl implements IVisitorLogService {

    @Autowired
    private VisitorLogDao visitorLogDao;

    @Override
    public long add(VisitorLog visitorLog) {
        return visitorLogDao.insert(visitorLog);
    }

    @Override
    public long countByIndex(String shortUrlIndex, Long beginTs, Long endTs) {

        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(shortUrlIndex)) {
            params.put("shortUrlIndex", shortUrlIndex);
        }
        if (beginTs != null) {
            params.put("beginTs", beginTs);
        }
        if (endTs != null) {
            params.put("endTs", endTs);
        }

        return visitorLogDao.countByIndex(params);
    }

    @Override
    public long countVisitorsByIndex(String shortUrlIndex, Long beginTs, Long endTs) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(shortUrlIndex)) {
            params.put("shortUrlIndex", shortUrlIndex);
        }
        if (beginTs != null) {
            params.put("beginTs", beginTs);
        }
        if (endTs != null) {
            params.put("endTs", endTs);
        }

        return visitorLogDao.countVisitorsByIndex(params);
    }

}
