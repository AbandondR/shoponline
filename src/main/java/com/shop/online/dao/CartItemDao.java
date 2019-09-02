package com.shop.online.dao;

import com.shop.online.model.CartItem;
import com.shop.online.vo.CartItemVo;

import java.util.List;
import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/4 0004
 */
public interface CartItemDao extends BaseDao<CartItem>{
    int cartItemCount(String itemCountHql, Map<String, String> params) throws Exception;

    List<CartItem> queryItem(String queryItemHql, Map<String, String> queryItemParams) throws Exception;

    List<CartItemVo> queryCartItemListByUser(String hql, String userId) throws Exception;

    int deleteItemBySkuId(String deleteHql, Map<String, String> params) throws Exception;

    int updateItemCount(String updateHql, Map<String,String> params) throws Exception;

    List<CartItemVo> queryItemsBySkuIds(String itemsHql, String id, String[] skuIds) throws Exception;

    List<CartItem> queryItemsBySkuIdsAndcartId(String cartIemHql, String id, String[] skuIds) throws Exception;
}
