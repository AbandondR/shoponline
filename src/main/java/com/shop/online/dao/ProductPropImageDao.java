package com.shop.online.dao;

import com.shop.online.model.ProductPropImage;

import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/23 0023
 */
public interface ProductPropImageDao extends BaseDao<ProductPropImage> {
    List<ProductPropImage> findByProdId(String prodPropImageHql, String id) throws Exception;
}
