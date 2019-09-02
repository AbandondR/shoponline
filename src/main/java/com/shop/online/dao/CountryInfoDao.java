package com.shop.online.dao;

import com.shop.online.model.CountryInfo;

import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/27 0027
 */
public interface CountryInfoDao extends BaseDao<CountryInfo>{

    List<CountryInfo> findStreet(String hql, String areaCode);
}
