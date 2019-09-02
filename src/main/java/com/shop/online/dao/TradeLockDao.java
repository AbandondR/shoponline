package com.shop.online.dao;

import com.shop.online.model.TradeLock;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/12 0012
 */
public interface TradeLockDao extends BaseDao<TradeLock>{
    int deleteOne(String hql, String itemToken);
}
