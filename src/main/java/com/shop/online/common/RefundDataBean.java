package com.shop.online.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/12 0012
 */
@Data
public class RefundDataBean implements Serializable {

    private String out_trade_no;

    private String trade_no;

    private String refund_amount;

    private String refund_reason;

    private String out_request_no;
}
