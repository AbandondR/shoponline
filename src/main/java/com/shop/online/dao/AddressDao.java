package com.shop.online.dao;

import com.shop.online.model.Address;

import java.util.List;
import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/27 0027
 */
public interface AddressDao extends BaseDao<Address> {
    List<Address> queryAddressByUser(String hql, String customerId) throws Exception;

    int removeAddress(String hql, Map<String,String> params) throws Exception;

    Address getOneAddress(String hql, Map<String,String> params);

    int updateAddr(String hql, Address address) throws Exception;

    int changeDefaultAddr(String cancelDefaultHql, Map<String, String> params) throws Exception;

    Address queryAddressById(String addressHql, String addressId, String id) throws Exception;
}
