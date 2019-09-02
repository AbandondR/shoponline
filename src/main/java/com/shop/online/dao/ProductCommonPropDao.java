package com.shop.online.dao;

import com.shop.online.model.ProductCommonProp;

import java.util.List;
import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/23 0023
 */
public interface ProductCommonPropDao extends BaseDao<ProductCommonProp>{
    List<ProductCommonProp> findSalePropByProId(String hql, Map<String, Object> parmas) throws Exception;
}
