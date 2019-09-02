package com.shop.online.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/12 0012
 */
@Data
public class DelayDataBean implements Serializable {

    private String[] skuIds;
    private String tradeNo;
    private String itemToken;
}
