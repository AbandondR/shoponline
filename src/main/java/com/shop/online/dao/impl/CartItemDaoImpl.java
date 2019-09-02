package com.shop.online.dao.impl;

import com.shop.online.dao.CartItemDao;
import com.shop.online.model.CartItem;
import com.shop.online.vo.CartItemVo;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/4 0004
 */
@Repository
public class CartItemDaoImpl extends BaseDaoImpl<CartItem> implements CartItemDao {
    @Override
    public int cartItemCount(String itemCountHql, Map<String, String> params) throws Exception {
        Query query = this.getCurrentSession().createQuery(itemCountHql);
        if(params!=null && !params.isEmpty()) {
            for(String key : params.keySet()){
                query.setParameter(key, params.get(key));
            }
        }
        return query.list().size();
    }

    @Override
    public List<CartItem> queryItem(String queryItemHql, Map<String, String> params) throws Exception {
        Query q = this.getCurrentSession().createQuery(queryItemHql);
        if(params!=null && !params.isEmpty()) {
            for(String key : params.keySet()){
                q.setParameter(key, params.get(key));
            }
        }
        return q.list();
    }

    @Override
    public List<CartItemVo> queryCartItemListByUser(String hql, String userId) throws Exception {
        Query q = this.getCurrentSession().createQuery(hql);
        q.setParameter("userId", userId);
        return q.list();
    }

    @Override
    public int deleteItemBySkuId(String deleteHql, Map<String, String> params) throws Exception {
        Query q = this.getCurrentSession().createQuery(deleteHql);
        if(params!=null && !params.isEmpty()) {
            for(String key : params.keySet()){
                q.setParameter(key, params.get(key));
            }
        }
        return q.executeUpdate();
    }

    @Override
    public int updateItemCount(String updateHql, Map<String, String> params) throws Exception {
        Query q = this.getCurrentSession().createQuery(updateHql);
        if(params!=null && !params.isEmpty()) {
            for(String key : params.keySet()){
                q.setParameter(key, params.get(key));
            }
        }
        return q.executeUpdate();
    }

    @Override
    public List<CartItemVo> queryItemsBySkuIds(String itemsHql, String id, String[] skuIds) throws Exception {
        Query q = this.getCurrentSession().createQuery(itemsHql);
        q.setParameter("cartId", id);
        q.setParameterList("skuIds", skuIds);
        return q.list();
    }

    @Override
    public List<CartItem> queryItemsBySkuIdsAndcartId(String cartIemHql, String id, String[] skuIds) throws Exception {
        Query q = this.getCurrentSession().createQuery(cartIemHql);
        q.setParameter("cartId", id);
        q.setParameterList("skuIds", skuIds);
        return q.list();
    }
}
