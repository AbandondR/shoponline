package com.shop.online.service.impl;

import com.shop.online.common.BaseJunit4Test;
import com.shop.online.common.PageBean;
import com.shop.online.constant.ProductStateConstant;
import com.shop.online.dao.ProductCataDao;
import com.shop.online.dao.ProductDao;
import com.shop.online.dao.PropNameDao;
import com.shop.online.model.ProductCata;
import com.shop.online.model.PropName;
import com.shop.online.model.PropValue;
import com.shop.online.service.ProductService;
import com.shop.online.vo.ProductDetailsVo;
import com.shop.online.vo.ProductIndexVo;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/19 0019
 */
public class ProductServiceImplTest extends BaseJunit4Test {

    @Resource
    private ProductDao productDao;


    @Test
    @Transactional
    @Rollback(false)
    public void findCatagory() throws Exception{
        String hql = "select new com.shop.online.vo.ProductIndexVo(p.id, p.productName, p.brandId, p.createTime, p.productNum, p.status, p.description, pm.pictureSnapshot) " +
                "from Product p , ProductMedia pm where p.id = pm.productId and p.status = :status order by p.createTime desc";
        String hql2 = "from Product p , ProductMedia pm where p.id = pm.productId and p.status = :status order by p.createTime desc";
        PageBean<ProductIndexVo> productIndexVoPageBean = new PageBean<>();
        List<ProductIndexVo> lists = productDao.findNewProductByPage(hql, ProductStateConstant.NEW_PRODUCT.getCode(), productIndexVoPageBean);
    }
    @Resource
    private ProductCataDao productCataDao;

    @Transactional
    @Rollback(false)
    public void findCatagory_2() throws Exception{
        String hql2 = "from ProductCata pc ";
        List<ProductCata> catas = productCataDao.findAllCata(hql2);
        Map<String, List<ProductCata>> map = catas.stream().collect(Collectors.groupingBy((e)->e.getParentId()));
        System.out.println("   ");
    }
    @Resource
    private PropNameDao propNameDao;
    @Test
    @Transactional
    @Rollback(false)
    public void findSaleProp() throws Exception {
        String hql = " from PropName pn where pn.cataId=:cataId ";

        List<PropName> propSets = propNameDao.findSearchPropByCata(hql, "1");

        for(PropName propName : propSets) {
            System.out.println(propName.getPropName());
            List<PropValue> values = propName.getPropValueList();

        }
    }

    @Resource
    private ProductService productService;
    @Test
    @Transactional
    @Rollback(true)
    public void productDetailsTest() throws Exception {
        String productId = "1";
        ProductDetailsVo detailsVos = productService.findProductDetailsById(productId);
        System.out.println("success");
    }
}