package com.shop.online.dao;

import com.shop.online.model.Product;
import com.shop.online.model.ProductSku;

import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/25 0025
 */
public interface ProductSkuDao extends BaseDao<ProductSku>{
    ProductSku QueryOneById(String skuHql, String skuId) throws Exception;

    int updateStockOrSaleNum(String hql, String skuId, int itemCount) throws Exception;
}
