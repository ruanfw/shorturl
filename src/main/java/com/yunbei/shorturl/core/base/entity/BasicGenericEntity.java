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
    public Date addTs;

    /**
     * 更新时的时间戳
     */
    public Date updateTs;

    public Date getAddTs() {
        return addTs;
    }

    public void setAddTs(Date addTs) {
        this.addTs = addTs;
    }

    public Date getUpdateTs() {
        return updateTs;
    }

    public void setUpdateTs(Date updateTs) {
        this.updateTs = updateTs;
    }
}
