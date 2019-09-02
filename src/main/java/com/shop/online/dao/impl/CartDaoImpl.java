package com.shop.online.dao.impl;

import com.shop.online.dao.ShopCartDao;
import com.shop.online.model.ShopCart;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/4 0004
 */
@Repository
public class CartDaoImpl extends BaseDaoImpl<ShopCart> implements ShopCartDao {
    @Override
    public ShopCart queryOne(String cartQueryHql, String userId) throws Exception {
        Query<ShopCart> q = this.getCurrentSession().createQuery(cartQueryHql);
        q.setParameter("userId", userId);
        return q.uniqueResult();
    }
}
