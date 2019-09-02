package com.shop.online.dao;

import com.shop.online.model.Trade;

import java.util.List;
import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/8 0008
 */
public interface TradeDao extends BaseDao<Trade>{
    Trade queryTradeByTradNum(String hql, String outTradeNo) throws Exception;

    List<Trade> queryChildTradeByTradeNum(String hql_5, String tradeId);

    Trade queryTradeByTradeNumAndUser(String hql, Map<String, String> params);
}
