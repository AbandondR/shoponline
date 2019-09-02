package com.shop.online.dao.impl;

import com.shop.online.dao.AddressDao;
import com.shop.online.model.Address;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/27 0027
 */
@Repository
public class AddressDaoImpl extends BaseDaoImpl<Address> implements AddressDao {

    @Override
    public List<Address> queryAddressByUser(String hql, String customerId) throws Exception {
        Query q = this.getCurrentSession().createQuery(hql);
        q.setParameter("customerId", customerId);
        return q.list();
    }

    @Override
    public int removeAddress(String hql, Map<String, String> params) throws Exception {
        Query q = this.getCurrentSession().createQuery(hql);
        if(params != null && !params.isEmpty()) {
            for(String key : params.keySet()) {
                q.setParameter(key, params.get(key));
            }
        }
        return q.executeUpdate();
    }

    @Override
    public Address getOneAddress(String hql, Map<String, String> params) {
        Query q = this.getCurrentSession().createQuery(hql);
        if(params != null && !params.isEmpty()) {
            for(String key : params.keySet()) {
                q.setParameter(key, params.get(key));
            }
        }
        return (Address) q.list().get(0);
    }

    @Override
    public int updateAddr(String hql, Address address) throws Exception {
        Query q = this.getCurrentSession().createQuery(hql);
        q.setProperties(address);
        return q.executeUpdate();
    }

    @Override
    public int changeDefaultAddr(String hql, Map<String, String> params) throws Exception {
        Query q = this.getCurrentSession().createQuery(hql);
        if(params != null && !params.isEmpty()) {
            for(String key : params.keySet()) {
                q.setParameter(key, params.get(key));
            }
        }
        return q.executeUpdate();
    }

    @Override
    public Address queryAddressById(String addressHql, String addressId, String id) throws Exception {
        Query<Address> q = this.getCurrentSession().createQuery(addressHql);
        q.setParameter("addressId", addressId);
        q.setParameter("userId", id);
        return q.uniqueResult();
    }
}
