package com.yunbei.shorturl.core.shorturl.entity;

import com.yunbei.shorturl.core.base.entity.BasicEntity;
import com.yunbei.shorturl.core.shorturl.enums.AccountSource;

public class ShortUrl extends BasicEntity {

    /**
     * 帐号
     */
    private String account;

    /**
     * 帐号来源 {@link AccountSource}
     */
    private int accountSource;

    /**
     * 生成的短链
     */
    private String shortUrlIndex;

    private String url;

    public ShortUrl() {
    }

    public ShortUrl(String account, String url) {
        this.account = account;
        this.url = url;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getShortUrlIndex() {
        return shortUrlIndex;
    }

    public void setShortUrlIndex(String shortUrlIndex) {
        this.shortUrlIndex = shortUrlIndex;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getAccountSource() {
        return accountSource;
    }

    public void setAccountSource(int accountSource) {
        this.accountSource = accountSource;
    }

    @Override
    public String toString() {
        return "ShortUrl [account=" + account + ", shortUrlIndex=" + shortUrlIndex + ", url=" + url + "]";
    }

}
