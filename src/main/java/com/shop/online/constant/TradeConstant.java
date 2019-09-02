package com.shop.online.constant;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/8 0008
 */
public class TradeConstant {

    //已取消
    public static final String CANCELED = "00000";

    //待支付
    public static final String UNPAID= "0000";


    //待发货
    public static final String UNSHIPPED = "0001";
    //待收货
    public static final String WAITGET = "0011";
    //已收货，待评价
    public static final String UNCOMMENTED = "0111";
    //已收货，已评价
    public static final String COMMENTED = "1111";
    //待收货，请求退货
    public static final String UNRECIV_RETURN = "010001";
    //待收货，退款
    public static final String UNRECIV_REFUND = "1100001";
    //已收货，退货
    public static final String RECIVED_RETURN = "010111";
    //已收货，退款
    public static final String RECIVED_REFUND = "110111";

    //在线支付
    public static final String ONLINE_PAY = "000";
    //货到付款
    public static final String RECIVED_PAY = "111";


}
