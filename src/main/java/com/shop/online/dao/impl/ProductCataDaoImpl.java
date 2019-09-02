package com.shop.online.dao.impl;

import com.shop.online.dao.ProductCataDao;
import com.shop.online.model.ProductCata;
import com.shop.online.model.PropName;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/20 0020
 */
@Repository
public class ProductCataDaoImpl extends BaseDaoImpl<ProductCata> implements ProductCataDao {

    @Override
    public List<ProductCata> findAllCata(String hql) throws Exception {
        Query q = this.getCurrentSession().createQuery(hql);
        return q.list();
    }

    @Override
    public List<ProductCata> findAllCataByPage(String hql, Map<String, String> params, int page, int rows) {
        Query q = this.getCurrentSession().createQuery(hql);
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                if (key.equals("nameCondition")) {
                    q.setParameter(key, "%" + params.get(key) + "%");
                } else {
                    q.setParameter(key, params.get(key));
                }
            }
        }
        return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
    }

    @Override
    public Integer countCata_admin(String hql, Map<String, String> params) {
        Query q = this.getCurrentSession().createQuery(hql);
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                if (key.equals("nameCondition")) {
                    q.setParameter(key, "%" + params.get(key) + "%");
                } else {
                    q.setParameter(key, params.get(key));
                }
            }
        }
        return q.list().size();
    }

    @Override
    public int updateCata(String hql, Map<String, String> params) {
        Query q = this.getCurrentSession().createQuery(hql);
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                q.setParameter(key, params.get(key));
            }
        }
        return q.executeUpdate();
    }

    @Override
    public ProductCata findCataById(String hql, String cataId) {
        Query<ProductCata> q = this.getCurrentSession().createQuery(hql);
        q.setParameter("id", cataId);
        return q.uniqueResult();

    }
}
