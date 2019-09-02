package com.shop.online.dao.impl;

import com.shop.online.dao.BrandDao;
import com.shop.online.model.Brand;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/22 0022
 */
@Repository
public class BrandDaoImpl extends BaseDaoImpl<Brand> implements BrandDao {
    @Override
    public List<Brand> findBrandByCata(String hql, String cataId) throws Exception {
        Query q = this.getCurrentSession().createQuery(hql);
        q.setParameter("cataId", cataId);
        return q.list();
    }
}
