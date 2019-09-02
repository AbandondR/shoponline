package com.shop.online.dao.impl;

import com.shop.online.common.PageBean;
import com.shop.online.dao.ProductDao;
import com.shop.online.model.Product;
import com.shop.online.vo.ProductDetailsVo;
import com.shop.online.vo.ProductIndexVo;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/19 0019
 */
@Repository
public class ProductDaoImpl extends BaseDaoImpl<Product> implements ProductDao {

    @Override
    public List<ProductIndexVo> findNewProductByPage(String hql, String param, PageBean pageBean) throws Exception {
        Query q = this.getCurrentSession().createQuery(hql);
        if(!StringUtils.isEmpty(param)) {
            q.setParameter("status", param);
        }
        return q.setFirstResult((pageBean.getCurrentPage()-1)*pageBean.getPageSize()).setMaxResults(pageBean.getPageSize()).list();

    }

    @Override
    public List<ProductIndexVo> findProductByCata(String hql, Map<String, Object> params, PageBean pageBean) throws Exception {
        Query q = this.getCurrentSession().createQuery(hql);
        if(!params.isEmpty() && params != null) {
            for (String key : params.keySet()) {
                q.setParameter(key, params.get(key));
            }
        }
        q.setFirstResult((pageBean.getCurrentPage()-1)*pageBean.getPageSize()).setMaxResults(pageBean.getPageSize());
        return q.list();
    }

    @Override
    public ProductDetailsVo findProductDetails(String hql, String id) throws Exception {
        Query<ProductDetailsVo> q = this.getCurrentSession().createQuery(hql);
        q.setParameter("id", id);
        return q.uniqueResult();
    }


}
