package com.shop.online.dao.impl;

import com.shop.online.dao.ProductPropImageDao;
import com.shop.online.model.ProductPropImage;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/23 0023
 */
@Repository
public class ProductPropImageDaoImpl extends BaseDaoImpl<ProductPropImage> implements ProductPropImageDao {
    @Override
    public List<ProductPropImage> findByProdId(String prodPropImageHql, String id) throws Exception {
        Query q = this.getCurrentSession().createQuery(prodPropImageHql);
        q.setParameter("productId", id);
        return q.list();
    }
}
