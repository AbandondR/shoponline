package com.shop.online.dao;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.shop.online.model.ProductCata;
import com.shop.online.model.PropName;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/20 0020
 */

public interface ProductCataDao extends BaseDao<ProductCata> {
    public List<ProductCata> findAllCata(String hql) throws Exception;

    List<ProductCata> findAllCataByPage(String hql_1, Map<String, String> params, int page, int rows);

    Integer countCata_admin(String hql_1, Map<String,String> params);

    int updateCata(String hql_1, Map<String,String> params);

    ProductCata findCataById(String hql_2, String cataId);
}
