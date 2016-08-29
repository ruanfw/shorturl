package com.yunbei.shorturl.core.base.dto;

import org.apache.commons.lang3.StringUtils;

public class BaseResult {

    private boolean success = true;

    private int code = 200;

    private String message = StringUtils.EMPTY;

    private Object results;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BaseResult() {
    }

    public Object getResults() {
        return results;
    }

    public void setResults(Object results) {
        this.results = results;
    }

    public BaseResult(String message) {
        this.message = message;
    }

    public BaseResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public BaseResult(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

}
