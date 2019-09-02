package com.shop.online.dao;

import com.shop.online.common.PageBean;
import com.shop.online.model.Product;
import com.shop.online.vo.ProductDetailsVo;
import com.shop.online.vo.ProductIndexVo;

import java.util.List;
import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/19 0019
 */
public interface ProductDao extends BaseDao<Product> {

    List<ProductIndexVo> findNewProductByPage(String hql, String param, PageBean pageBean) throws Exception;
    List<ProductIndexVo> findProductByCata(String hql, Map<String, Object> params, PageBean pageBean) throws Exception;

    ProductDetailsVo findProductDetails(String hql, String id) throws Exception;
}
