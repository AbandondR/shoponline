package com.shop.online.dao.impl;

import com.shop.online.dao.TradeDao;
import com.shop.online.model.Trade;
import org.hibernate.LockMode;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/8 0008
 */
@Repository
public class TradeDaoImpl extends BaseDaoImpl<Trade> implements TradeDao {
    @Override
    public Trade queryTradeByTradNum(String hql, String outTradeNo) throws Exception {
        Query<Trade> q = this.getCurrentSession().createQuery(hql);
        q.setParameter("tradeNum", outTradeNo);
        //意向排他锁
        q.setLockMode("t", LockMode.PESSIMISTIC_WRITE);
        return q.uniqueResult();
    }

    @Override
    public List<Trade> queryChildTradeByTradeNum(String hql, String tradeId) {
        Query q = this.getCurrentSession().createQuery(hql);
        q.setParameter("parentNum", tradeId);
        q.setLockMode("t", LockMode.PESSIMISTIC_WRITE);
        return q.list();
    }

    @Override
    public Trade queryTradeByTradeNumAndUser(String hql, Map<String, String> params) {
        Query<Trade> q = this.getCurrentSession().createQuery(hql);
        if(params!=null&& !params.isEmpty()) {
            for(String key : params.keySet()) {
                q.setParameter(key, params.get(key));
            }
        }
        q.setLockMode("t", LockMode.PESSIMISTIC_WRITE);
        return q.uniqueResult();
    }
}
