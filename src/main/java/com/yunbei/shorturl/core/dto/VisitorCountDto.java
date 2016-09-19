package com.yunbei.shorturl.core.dto;

public class VisitorCountDto {

    private long uv;

    private long pv;

    public long getUv() {
        return uv;
    }

    public VisitorCountDto setUv(long uv) {
        this.uv = uv;
        return this;
    }

    public long getPv() {
        return pv;
    }

    public VisitorCountDto setPv(long pv) {
        this.pv = pv;
        return this;
    }

    @Override
    public String toString() {
        return "VisitorCountDto [uv=" + uv + ", pv=" + pv + "]";
    }

}
