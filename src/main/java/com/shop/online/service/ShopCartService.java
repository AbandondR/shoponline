package com.shop.online.service;

import com.shop.online.model.CartItem;
import com.shop.online.model.ProductSku;
import com.shop.online.vo.CartItemVo;

import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/4 0004
 */

public interface ShopCartService {
    int addProductAndNum(ProductSku productSku, String skuGroupStr, String userId, String productNum) throws Exception;

    int deleteItem(String userId, String skuId) throws Exception;

    String updateItemCount(String skuId, String itemCount, String userId) throws Exception;

    List<CartItemVo> queryItemsBySkuId(String userId, String[] skuIds) throws Exception;

    void generateCart(String userId);
}
