package com.shop.online.service;

import com.shop.online.model.Address;
import com.shop.online.model.CountryInfo;

import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/27 0027
 */
public interface AddressService {

    int  insertOne(Address address) throws Exception;

    List<Address> queryAllAddressByUser(String id) throws Exception;

    int deleteAddressById(String addressId, String userId) throws Exception;

    Address queryOneAddress(String addressId, String userId) throws Exception;

    int updateAddress(Address address) throws Exception;

    int changeDefaultAddr(String addressId, String userId) throws Exception;
}
