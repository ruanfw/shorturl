package com.yunbei.shorturl.core.entity;

import com.yunbei.shorturl.core.base.entity.BasicEntity;

public class VisitorLog extends BasicEntity {

    /**
     * 访问时间
     */
    private Long visitorTime;

    /**
     * 访问ip
     */
    private String ip;

    /**
     * 访问的短链接索引
     */
    private String shortUrlIndex;

    /**
     * 短链接转换的真实链接
     */
    private String realUrl;

    public VisitorLog() {
    }

    public VisitorLog(Long visitorTime, String ip, String shortUrlIndex, String realUrl) {
        this.visitorTime = visitorTime;
        this.ip = ip;
        this.shortUrlIndex = shortUrlIndex;
        this.realUrl = realUrl;
    }

    public Long getVisitorTime() {
        return visitorTime;
    }

    public void setVisitorTime(Long visitorTime) {
        this.visitorTime = visitorTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getShortUrlIndex() {
        return shortUrlIndex;
    }

    public void setShortUrlIndex(String shortUrlIndex) {
        this.shortUrlIndex = shortUrlIndex;
    }

    public String getRealUrl() {
        return realUrl;
    }

    public void setRealUrl(String realUrl) {
        this.realUrl = realUrl;
    }

    @Override
    public String toString() {
        return "VisitorLog [visitorTime=" + visitorTime + ", ip=" + ip + ", shortUrlIndex=" + shortUrlIndex
                + ", realUrl=" + realUrl + "]";
    }

}
