/**
 * 
 */
package com.yunbei.shorturl.core.base.enums;

/**
 * 
 * @title ReturnCodeMsg.java
 * @author lizhong.chen
 * @data 2013-12-12下午12:20:17
 * @description 接口返回的code和msg
 * @version V1.0
 * 
 */
public enum ReturnCodeMsg {
    /**
     * 
     */
    SUCCESS(200, "操作正确"),

    URL_IS_NULL(300, "网址不能为空")

    ;
    ReturnCodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
