package com.shop.online.service.impl;

import com.shop.online.dao.AddressDao;
import com.shop.online.model.Address;
import com.shop.online.service.AddressService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/27 0027
 */
@Service
public class AddressSerciceImpl implements AddressService {

    @Resource
    private AddressDao addressDao;

    @Override
    public int insertOne(Address address) throws Exception {
        String queryAddressHql = "from Address a where a.customerId =:customerId";
        List<Address> addressList = addressDao.queryAddressByUser(queryAddressHql, address.getCustomerId());
        //用户是否已经添加地址
        if(addressList.isEmpty()) {
            //设为默认地址
            address.setIsDefault("1");
        }
        else {
            address.setIsDefault("0");
        }
        String id = (String)addressDao.save(address);
        return 0;
    }

    @Override
    public List<Address> queryAllAddressByUser(String id) throws Exception {
        //按照默认收货地址排序
        String queryAddressHql = "from Address a where a.customerId =:customerId order by a.isDefault desc ";
        List<Address> addressList = addressDao.queryAddressByUser(queryAddressHql, id);
        //处理电话
        List<Address> addressList1 = addressList.stream().filter(address -> {
            String telephone = address.getTelephone();
            int length = telephone.length();
            if(!StringUtils.isEmpty(telephone) && length>=11) {
                String temp = telephone.substring(0,2).concat("****").concat(telephone.substring(7));
                address.setTelephone(temp);
            }
            return true;
        }).collect(Collectors.toList());
        return addressList1;
    }

    @Override
    public int deleteAddressById(String addressId, String userId) throws Exception {
        String hql = "delete from Address a where a.id=:addressId and a.customerId=:userId";
        Map<String, String> params = new HashMap<>();
        params.put("addressId", addressId);
        params.put("userId", userId);
        return addressDao.removeAddress(hql, params);
    }

    @Override
    public Address queryOneAddress(String addressId, String userId) throws Exception{
        String hql = "from Address a where a.id = :addressId and a.customerId=:userId";
        Map<String, String> params = new HashMap<>();
        params.put("addressId", addressId);
        params.put("userId", userId);
        return addressDao.getOneAddress(hql, params);

    }

    @Override
    public int updateAddress(Address address) throws Exception {
        String hql = "update Address a set a.province=:province, a.city=:city, a.area=:area, a.street=:street, a.userName=:userName" +
                ", a.telephone=:telephone, a.postalCode=:postalCode, a.addressDetail=:addressDetail where a.id=:id and a.customerId=:customerId";
        return addressDao.updateAddr(hql, address);
    }

    @Override
    public int changeDefaultAddr(String addressId, String userId) throws Exception {
        //取消原来的默认地址
        String cancelDefaultHql = "update  Address a set a.isDefault='0' where a.customerId=:userId and a.isDefault='1'";
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        addressDao.changeDefaultAddr(cancelDefaultHql, params);
        //设置新的默认地址
        String setDefaultHql = "update Address a set a.isDefault='1' where a.id=:addressId and a.customerId=:userId";
        Map<String, String> params1 = new HashMap<>();
        params1.put("addressId", addressId);
        params1.put("userId", userId);
        return addressDao.changeDefaultAddr(setDefaultHql, params1);
    }
}
