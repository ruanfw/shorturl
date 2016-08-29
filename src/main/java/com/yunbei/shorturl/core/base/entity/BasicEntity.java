package com.yunbei.shorturl.core.base.entity;

import java.io.Serializable;

/**
 * 基类，继承自{@link BasicGenericEntity} ，扩展ID
 * 
 */
public class BasicEntity extends BasicGenericEntity implements Serializable {
    /**
     * 主键自增长
     */
    public Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
