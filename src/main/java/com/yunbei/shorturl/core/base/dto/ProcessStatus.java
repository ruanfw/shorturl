package com.yunbei.shorturl.core.base.dto;

import java.io.Serializable;

/**
 * 处理状态
 * 
 * @author ly
 * 
 * @created 2014-5-3 上午1:32:17
 * 
 */
public class ProcessStatus implements Serializable {

    /**
     * true or false.
     */
    private boolean isSuccess;

    /**
     * when false, return message.
     */
    private String message;

    private Object result;

    public ProcessStatus(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public ProcessStatus(boolean isSuccess, String message, Object result) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.result = result;
    }

    public ProcessStatus(boolean isSuccess) {
        this.isSuccess = isSuccess;
        this.message = "";
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ProcessStatus [isSuccess=" + isSuccess + ", message=" + message + ", result=" + result + "]";
    }

}