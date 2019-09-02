package com.shop.online.dao;

import com.shop.online.model.Order;

import java.util.List;
import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/9 0009
 */
public interface OrderDao extends BaseDao<Order>{
    List<Order> queryOrderByTradeId(String hql, String tradeId);

    int updateOrderStatusByTradeId(String hql, String code, String id);

    List<Order> queryOrderPageByUser(String hql, Map<String,Object> params, int currPage, int pageSize);

    Long countOrderByUser(String hql_2, Map<String,Object> params);

    List<Order> countStateByUser(String hql_1, String userId);

    Order queryOrderByOrderNum(String hql_1, String orderNum, String userId);

    int updateOrderStatusById(String hql_1, Map<String, String> params);

    List<Order> queryOrder(String hql_1, Map<String,String> params, Integer currPage, Integer pageSize);

    Long countOrder_admin(String hql_1, Map<String,String> params);

    Order queryOrderByOrderNum_admin(String hql_1, Map<String,String> params);
}
