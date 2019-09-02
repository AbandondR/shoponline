package com.shop.online.service;

import com.shop.online.common.PageBean;
import com.shop.online.model.Brand;
import com.shop.online.model.ProductCata;
import com.shop.online.model.PropName;

import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/21 0021
 */
public interface ProductCataService {
    List<PropName> findSearchPropByCata(String cataId) throws Exception;

    List<Brand> findBrandByCata(String cataId) throws Exception;

    PageBean<ProductCata> getAllCategory_admin(int page, String idCondition, String nameCondition) throws Exception;

    String updateCataName(String cataId, String cataName);

    ProductCata findCataById(String idCondition);

    String updateCataStatus(String id, String status);

    List<ProductCata> findCategoryByPid(String id);

    String insertOne(ProductCata productCata) throws Exception;
}
