package com.shop.online.service;

import com.shop.online.common.PageBean;
import com.shop.online.model.ProductCata;
import com.shop.online.model.ProductSku;
import com.shop.online.model.PropName;
import com.shop.online.vo.ProdSkuGroupVo;
import com.shop.online.vo.ProductDetailsVo;
import com.shop.online.vo.ProductIndexVo;

import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/18 0018
 */
public interface ProductService {
    PageBean<ProductIndexVo> findNewProduct() throws Exception;

    List<ProductCata> findCategory() throws Exception;

    PageBean<ProductIndexVo> findProductByCata(String cataId, Integer page, String sort) throws Exception;

    ProductDetailsVo findProductDetailsById(String id) throws Exception;

    ProdSkuGroupVo findSkuPropAndProd(String productId) throws Exception;

    List<PropName> findAllProps(String cataId);
}
