package com.shop.online.dao;

import com.shop.online.model.Brand;

import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/22 0022
 */
public interface BrandDao extends BaseDao<Brand>{
    public List<Brand> findBrandByCata(String hql, String cataId) throws Exception;
}
