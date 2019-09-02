package com.shop.online.dao.impl;

import com.shop.online.dao.CountryInfoDao;
import com.shop.online.model.CountryInfo;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/27 0027
 */
@Repository
public class CountryInfoDaoImpl extends BaseDaoImpl<CountryInfo> implements CountryInfoDao {
    @Override
    public List<CountryInfo> findStreet(String hql, String areaCode) {
        Query q = this.getCurrentSession().createQuery(hql);
        q.setParameter("areaCode", areaCode);
        return q.list();
    }
}
