package com.shop.online.dao.impl;

import com.shop.online.dao.TradeLockDao;
import com.shop.online.model.TradeLock;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/12 0012
 */
@Repository
public class TradeLockDaoImpl extends BaseDaoImpl<TradeLock> implements TradeLockDao {
    @Override
    public int deleteOne(String hql, String itemToken) {
        Query query = this.getCurrentSession().createQuery(hql);
        query.setParameter("tradeToken", itemToken);
        return query.executeUpdate();
    }
}
