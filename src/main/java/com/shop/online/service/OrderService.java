package com.shop.online.service;

import com.shop.online.common.PageBean;
import com.shop.online.common.RefundDataBean;
import com.shop.online.model.Order;
import com.shop.online.model.Trade;
import com.shop.online.model.User;

import java.util.List;
import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/5 0005
 */
public interface OrderService {
    Map<String, String> addOrder(String[] skuIds, String addressId, User user) throws Exception;

    int countTradeByItemToken(String itemToken, String userId) throws Exception;

    Trade queryTradeById(String outTradeNo) throws Exception;

    void updateTrade(Trade trade) throws Exception;

    Integer addTradeLock(String itemToken) throws Exception;

    int deleteTradeLock(String itemToken);

    List<Order> queryOrderByTradeId(String id);

    int updateOrderStatusByTradeId(String code, String id);

    PageBean<Order> queryOrderByUser(int currPage, String state, String userId) throws Exception;

    Map<String, Long> countStateByUser(String userId);

    Map<String, String> queryOrderForPay(String orderId, String userId) throws Exception;

    int updateOrderStatusById(String orderId, String userId, String state, String tradeId) throws Exception;

    String aliPayCallBack(Map<String, String[]> requestParams) throws Exception;

    Order queryOrderByOrderNum(String orderId, String userId) throws Exception;

    String isTakeGoodes(String orderId, String userId);

    Order queryOrderDetails(String orderNum, String id) throws Exception;

    PageBean<Order> queryOrderByPage(Integer currPage, String condition, String state);

    Order queryOrderDetails_admin(String orderNum) throws Exception;

    void updateOrderStatusById_admin(String orderNum, String state) throws Exception;
}
