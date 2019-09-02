package com.shop.online.dao;

import com.shop.online.model.ProductMedia;

import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/23 0023
 */
public interface ProductMediaDao extends BaseDao<ProductMedia>{
    List<ProductMedia> findProMediaByProId(String proMediaHql, String productId) throws Exception;
}
