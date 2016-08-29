package com.yunbei.shorturl.core.shorturl.enums;

public enum AccountSource {

    UNKNOW(1, "未知"),

    DINGDONG(2, "叮咚"),

    DINGDONGYUN(3, "叮咚云");

    private int source;

    private String descr;

    private AccountSource(int source, String descr) {
        this.source = source;
        this.descr = descr;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

}
