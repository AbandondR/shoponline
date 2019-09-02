package com.shop.online.dao.impl;

import com.shop.online.dao.OrderDao;
import com.shop.online.model.Order;
import org.hibernate.LockMode;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/9 0009
 */
@Repository
public class OrderDaoImpl extends BaseDaoImpl<Order> implements OrderDao {
    @Override
    public List<Order> queryOrderByTradeId(String hql, String tradeId) {
        Query q = this.getCurrentSession().createQuery(hql);
        q.setParameter("tradeId", tradeId);
        return q.list();
    }

    @Override
    public int updateOrderStatusByTradeId(String hql, String code, String id) {
        Query q = this.getCurrentSession().createQuery(hql);
        q.setParameter("orderStatus", code);
        q.setParameter("tradeId", id);
        return q.executeUpdate();
    }

    @Override
    public List<Order> queryOrderPageByUser(String hql, Map<String, Object> params, int currPage, int pageSize) {

        return this.find(hql, params, currPage, pageSize);
        /*Query q = this.getCurrentSession().createQuery(hql);
        if(params!=null && !params.isEmpty()) {
            for(String key : params.keySet()) {
                q.setParameter(key, params.get(key));
            }
        }*/
    }

    @Override
    public Long countOrderByUser(String hql, Map<String, Object> params) {
        Query q = this.getCurrentSession().createQuery(hql);
        if(params!=null && !params.isEmpty()) {
            for(String key : params.keySet()) {
                q.setParameter(key, params.get(key));
            }
        }
        return (Long) q.list().get(0);
    }

    @Override
    public List<Order> countStateByUser(String hql, String userId) {
        Query q = this.getCurrentSession().createQuery(hql);
        q.setParameter("userId", userId);
        return q.list();
    }

    @Override
    public Order queryOrderByOrderNum(String hql, String orderNum, String userId) {
        Query<Order> q = this.getCurrentSession().createQuery(hql);
        q.setParameter("orderNum", orderNum);
        q.setParameter("userId", userId);
        q.setLockMode("o", LockMode.PESSIMISTIC_WRITE);
        return q.uniqueResult();
    }

    @Override
    public int updateOrderStatusById(String hql, Map<String, String> params) {
        Query q = this.getCurrentSession().createQuery(hql);
        if(params!=null && !params.isEmpty()) {
            for(String key : params.keySet()) {
                q.setParameter(key, params.get(key));
            }
        }
        return q.executeUpdate();
    }

    @Override
    public List<Order> queryOrder(String hql, Map<String, String> params, Integer currPage, Integer pageSize) {
        Query q = this.getCurrentSession().createQuery(hql);
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                if(key.equals("orderNum")) {
                    q.setParameter(key, "%"+params.get(key)+"%");
                }
                else {
                    q.setParameter(key, params.get(key));
                }
            }
        }
        return q.setFirstResult((currPage - 1) * pageSize).setMaxResults(pageSize).list();
    }

    @Override
    public Long countOrder_admin(String hql, Map<String, String> params) {
        Query q = this.getCurrentSession().createQuery(hql);
        if(params!=null && !params.isEmpty()) {
            for(String key : params.keySet()) {
                q.setParameter(key, params.get(key));
            }
        }
        return (Long) q.list().get(0);
    }

    @Override
    public Order queryOrderByOrderNum_admin(String hql, Map<String, String> params) {
        Query q = this.getCurrentSession().createQuery(hql);
        if(params!=null && !params.isEmpty()) {
            for(String key : params.keySet()) {
                q.setParameter(key, params.get(key));
            }
        }
        return (Order)q.list().get(0);
    }
}
