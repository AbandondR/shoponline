package com.shop.online.dao;

import com.shop.online.model.ShopCart;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/4 0004
 */
public interface ShopCartDao extends BaseDao<ShopCart>{
    ShopCart queryOne(String cartQueryHql, String userId) throws Exception;
}
