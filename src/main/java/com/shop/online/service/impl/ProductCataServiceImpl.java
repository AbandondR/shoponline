package com.shop.online.service.impl;

import com.shop.online.common.PageBean;
import com.shop.online.constant.PropNameConstant;
import com.shop.online.dao.BrandDao;
import com.shop.online.dao.ProductCataDao;
import com.shop.online.dao.PropNameDao;
import com.shop.online.model.Brand;
import com.shop.online.model.ProductCata;
import com.shop.online.model.PropName;
import com.shop.online.service.ProductCataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/21 0021
 */
@Service
public class ProductCataServiceImpl implements ProductCataService {

    public static final int PAGE_SIZE = 5;
    @Resource
    private ProductCataDao productCataDao;

    @Resource
    private PropNameDao propNameDao;

    @Resource
    private BrandDao brandDao;

    @Override
    public List<PropName> findSearchPropByCata(String cataId) throws Exception {

        StringBuilder hqlConn = new StringBuilder();
        String hql = " from PropName pn where 1=1 ";
        if (!StringUtils.isEmpty(cataId)) {
            hqlConn.append(" and pn.cataId=:cataId ");
        }
        hqlConn.append("and pn.isSearchProp = " + PropNameConstant.SEARCH_PROP_OK);
        hql += hqlConn.toString();
        List<PropName> propSets = propNameDao.findSearchPropByCata(hql, cataId);
        return propSets;
    }

    @Override
    public List<Brand> findBrandByCata(String cataId) throws Exception {
        String hql = "from Brand b where b.cataId=:cataId";
        List<Brand> brands = brandDao.findBrandByCata(hql, cataId);
        return brands;
    }

    @Override
    public PageBean<ProductCata> getAllCategory_admin(int page, String idCondition, String nameCondition) throws Exception {
        String hql_1 = "from ProductCata pc where 1=1 ";
        StringBuilder builder = new StringBuilder();
        PageBean pageBean = new PageBean();
        pageBean.setCurrentPage(page);
        pageBean.setPageSize(PAGE_SIZE);
        Map<String, String> params = new HashMap<>();
        if (!StringUtils.isEmpty(idCondition)) {
            builder.append("and pc.parentId=:parentId ");
            params.put("parentId", idCondition);
        }
        if (!StringUtils.isEmpty(nameCondition)) {
            builder.append("and pc.cataName like :nameCondition ");
            params.put("nameCondition", nameCondition);
        }
        hql_1 += builder.toString();
        hql_1 += "order by pc.id+0 ";
        List<ProductCata> cataList = productCataDao.findAllCataByPage(hql_1, params, page, PAGE_SIZE);
        Integer count_obj = productCataDao.countCata_admin(hql_1, params);
        pageBean.setResultList(cataList);
        pageBean.setTotalCount(count_obj == null ? 0 : count_obj.intValue());
        return pageBean;
    }

    @Override
    public String updateCataName(String cataId, String cataName) {

        String hql_1 = "update ProductCata pc set pc.cataName=:cataName where pc.id=:id";
        String hql_2 = "from ProductCata pc where pc.id=:id";
        String hql_3;
        Map<String, Object> params_2 = new HashMap<>();
        ProductCata productCata = productCataDao.findCataById(hql_2, cataId);
        //判定当前级别下是否存在相同的cataName
        if (productCata == null) {
            return "error";
        }
        hql_3 = "from ProductCata pc where pc.parentId=:parentId and pc.cataName=:cataName";
        params_2.put("cataName", cataName);
        params_2.put("parentId", productCata.getParentId());
        List<ProductCata> productCatas = productCataDao.find(hql_3, params_2);
        if(!productCatas.isEmpty()) {
            return "repeat";
        }
        Map<String, String> params = new HashMap<>();
        params.put("id", cataId);
        params.put("cataName", cataName);
        int changCount = productCataDao.updateCata(hql_1, params);
        if(changCount == 0) {
            return "error";
        }
        return "success";
    }

    @Override
    public ProductCata findCataById(String idCondition) {
        String hql = "from ProductCata pc where pc.id=:id";
        return productCataDao.findCataById(hql, idCondition);
    }

    @Override
    public String updateCataStatus(String id, String status) {
        if(StringUtils.isEmpty(status) || StringUtils.isEmpty(id)) {
            return "paramet_error";
        }
        String hql = "update ProductCata pc set pc.status=:status where pc.id=:id";
        Map<String, String> params = new HashMap<>();
        params.put("status", status);
        params.put("id", id);
        int count = productCataDao.updateCata(hql, params);
        if(count == 0) {
            return "error";
        }
        return "success";
    }

    @Override
    public List<ProductCata> findCategoryByPid(String id) {
        if(StringUtils.isEmpty(id)) {
            id = "0";
        }
        String hql = "from ProductCata pc where pc.parentId=:parentId and pc.status='1'";
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", id);
        List<ProductCata> productCatas = productCataDao.find(hql, params);
        return productCatas;
    }

    @Override
    public String insertOne(ProductCata productCata) throws Exception {
        if(StringUtils.isEmpty(productCata.getParentId())) {
            productCata.setParentId("0");
            productCata.setIsParent(true);
        }
        else {
            productCata.setIsParent(false);
        }
        if(StringUtils.isEmpty(productCata.getCataName()) || StringUtils.isEmpty(productCata.getStatus())) {
            return "param_error";
        }
        //判断当前分级下，是否存在该分类名称
        String hql_1 = "from ProductCata pc where pc.parentId=:parentId and pc.cataName=:cataName ";
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", productCata.getParentId());
        params.put("cataName", productCata.getCataName());
        List<ProductCata> productCatas = productCataDao.find(hql_1, params);
        if(!productCatas.isEmpty()) {
            return "repeat";
        }
        StringBuilder idBuilder = new StringBuilder();
        Random random = new Random();
        for(int i=0; i< 5; i++) {
            idBuilder.append(random.nextInt(10));
        }
        productCata.setId(idBuilder.toString());
        //如果不是根节点，需要更新节点为父节点
        if(!productCata.getParentId().equals("0")) {
            String hql_2 = "from ProductCata pc where pc.id=:id ";
            ProductCata productCata1 = productCataDao.findCataById(hql_2, productCata.getParentId());
            if(productCata == null) {
                throw new Exception("param_error");
            }
            productCata1.setIsParent(true);
            productCataDao.update(productCata1);
        }
        productCataDao.save(productCata);
        return "success";
    }


}
