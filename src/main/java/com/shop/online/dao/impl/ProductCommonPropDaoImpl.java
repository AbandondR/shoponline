package com.shop.online.dao.impl;

import com.shop.online.dao.ProductCommonPropDao;
import com.shop.online.model.ProductCommonProp;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/23 0023
 */
@Repository
public class ProductCommonPropDaoImpl extends BaseDaoImpl<ProductCommonProp> implements ProductCommonPropDao {

    @Override
    public List<ProductCommonProp> findSalePropByProId(String hql, Map<String, Object> parmas) throws Exception {
        return this.find(hql, parmas);
    }
}
