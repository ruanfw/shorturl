package com.yunbei.shorturl.core.service.impl;

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

}
