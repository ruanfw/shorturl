package com.yunbei.shorturl.core.base.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 基类，包含插入字段和更新字段
 * 
 */
public class BasicGenericEntity implements Serializable {
    /**
     * 插入时的时间戳
     */
    public Date gmtCreated;

    /**
     * 更新时的时间戳
     */
    public Date gmtModified;

    public Date getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(Date gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

}
