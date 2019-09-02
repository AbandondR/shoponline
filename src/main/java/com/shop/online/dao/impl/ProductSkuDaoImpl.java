package com.shop.online.dao.impl;

import com.shop.online.dao.ProductSkuDao;
import com.shop.online.model.ProductSku;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/25 0025
 */
@Repository
public class ProductSkuDaoImpl extends BaseDaoImpl<ProductSku> implements ProductSkuDao {
    @Override
    public ProductSku QueryOneById(String skuHql, String skuId) throws Exception {
        Query<ProductSku> q = this.getCurrentSession().createQuery(skuHql);
        q.setParameter("skuId", skuId);
        return q.uniqueResult();
    }

    @Override
    public int updateStockOrSaleNum(String hql, String skuId, int itemCount) throws Exception {
        Query<ProductSku> q = this.getCurrentSession().createQuery(hql);
        //q.setParameter("stock", stock);
        q.setParameter("skuId", skuId);
        q.setParameter("itemCount", itemCount);
        return q.executeUpdate();
    }


}
