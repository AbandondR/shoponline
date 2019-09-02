package com.shop.online.common;

import lombok.Data;

import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/5/20 0020
 */
@Data
public class SendRedirectObj {

    //请求的资源
    private String requestUri;
    //HTTP方法
    private String methodType;
    //请求类型
    private String flag;
    //请求参数
    private Map<String, String[]> params;
}
