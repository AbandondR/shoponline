package com.shop.online.constant;

import lombok.Data;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/5/20 0020
 */
public enum HttpRequestType {
    ASYNC_REQUEST("异步请求", "000"),
    SYNC_REQUEST("同步请求", "111");
    String desc;
    String code;
    HttpRequestType(String desc, String code) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
