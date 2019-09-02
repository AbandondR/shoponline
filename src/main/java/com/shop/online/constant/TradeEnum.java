package com.shop.online.constant;

import lombok.Data;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/12 0012
 */
public enum TradeEnum {
    //trade状态，order状态
    CANCELED("00000", "已取消"),
    UNPAID("0000", "待支付"),
    FINISHED("11111", "已完成"),
    INVALID("000000", "失效"),
    VALID("111111", "有效"),
    //order状态
    UNSHIPPED("0001", "待发货"),
    WAITGET("0011", "待收货"),
    UNCOMMENTED("0111", "待评价"),
    COMMENTED("1111", "已评价"),
    UNRECIV_RETURN("010001", "待收货，请求退货"),
    UNRECIV_REFUND("1100001", "待收货，退款"),
    RECIVED_RETURN("010111", "已收货，退货"),
    RECIVED_REFUND("110111", "已收货，退款"),
    ONLINE_PAY("000", "线上支付"),
    RECIVED_PAY("111", "货到付款");

    private String code;
    private String desc;

    TradeEnum(String code, String desc) {
        this.desc = desc;
        this.code = code;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getCode() {
        return this.code;
    }

    public static String getDescByCode(String code) {
        for(TradeEnum tradeEnum : TradeEnum.values()) {
            if(tradeEnum.getCode().equals(code)) {
                return tradeEnum.desc;
            }
        }
        return null;
    }

    public static String getCodeByDesc(String desc) {
        for(TradeEnum tradeEnum : TradeEnum.values()){
            if(tradeEnum.getDesc().equals(desc)) {
                return tradeEnum.getCode();
            }
        }
        return null;
    }

}

