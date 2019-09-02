package com.shop.online.dao.impl;

import com.shop.online.dao.ProductMediaDao;
import com.shop.online.model.ProductMedia;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/23 0023
 */
@Repository
public class ProductMediaDaoImpl extends BaseDaoImpl<ProductMedia> implements ProductMediaDao {
    @Override
    public List<ProductMedia> findProMediaByProId(String proMediaHql, String productId) throws Exception {
        Query q = this.getCurrentSession().createQuery(proMediaHql);
        q.setParameter("productId", productId);
        return q.list();
    }
}
