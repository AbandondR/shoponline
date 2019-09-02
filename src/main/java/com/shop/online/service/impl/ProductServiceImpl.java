package com.shop.online.service.impl;

import com.shop.online.common.PageBean;
import com.shop.online.constant.ProductStateConstant;
import com.shop.online.constant.PropNameConstant;
import com.shop.online.constant.SortAttributeConstant;
import com.shop.online.dao.*;
import com.shop.online.model.*;
import com.shop.online.service.ProductService;
import com.shop.online.vo.ProdSkuGroupVo;
import com.shop.online.vo.ProductDetailsVo;
import com.shop.online.vo.ProductIndexVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/18 0018
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductDao productDao;

    @Resource
    private ProductCataDao productCataDao;

    @Resource
    private ProductCommonPropDao productCommonPropDao;

    @Resource
    private PropNameDao propNameDao;

    private final String hql = "select new com.shop.online.vo.ProductIndexVo(p.id, p.productName, p.brandId, p.createTime, p.productNum, p.status, p.priceListId, p.description, p.pictureSnapshot, p.saleTotalNum) ";
    /**
     * 最新上架商品
     * @return
     * @throws Exception
     */
    @Override
    public PageBean<ProductIndexVo> findNewProduct() throws Exception {
        String hql = "select new com.shop.online.vo.ProductIndexVo(p.id, p.productName, p.brandId, p.createTime, p.productNum, p.status, p.priceListId, p.description, p.pictureSnapshot, p.saleTotalNum) " +
                "from Product p , ProductMedia pm where p.id = pm.productId and p.status = :status order by p.createTime desc";

        PageBean<ProductIndexVo> productIndexVoPageBean = new PageBean<>();
        List<ProductIndexVo> lists = productDao.findNewProductByPage(hql, ProductStateConstant.NEW_PRODUCT.getCode(), productIndexVoPageBean);
        productIndexVoPageBean.setResultList(lists);
        Integer total = productDao.count(hql, new HashMap<String, Object>(){{
            this.put("status", ProductStateConstant.NEW_PRODUCT.getCode());
        }});
        productIndexVoPageBean.setTotalCount(total!=null ? total.intValue() : 0);
        return productIndexVoPageBean;
    }


    @Resource
    private ProductMediaDao productMediaDao;

    @Resource
    private ProductPropImageDao productPropImageDao;

    @Resource
    private EvaluateDao evaluateDao;

    @Override
    public ProductDetailsVo findProductDetailsById(String id) throws Exception{
        String hql = "select new com.shop.online.vo.ProductDetailsVo(p.id, p.productName, p.cataId, p.saleTotalNum, p.totalStock, p.productNum,p.priceListId, p.description)" +
                "from Product p where 1=1 and ";
        if(StringUtils.isEmpty(id)) {
            throw new Exception("商品id为空");
        }
        hql += "p.id = :id";
        ProductDetailsVo productDetailsVo = productDao.findProductDetails(hql, id);
        if(productDetailsVo == null) {
            throw new Exception("商品{" + id +"}" + "查询失败");
        }
        //商品属性图片查询
        String prodPropImageHql = "from ProductPropImage ppi where ppi.productId=:productId and ppi.isSkuProp='0'";
        List<ProductPropImage> productPropImages = productPropImageDao.findByProdId(prodPropImageHql, id);

        //商品基本属性查询(excluded销售属性)
        String commonPropHql = "from ProductCommonProp pcp where 1=1 and pcp.productId = :productId and pcp.status = :status and pcp.isSku='0'";
        Map<String,Object> params = new HashMap<>();
        params.put("productId", id);
        //属性状态:启用
        params.put("status", PropNameConstant.PROP_STATE_ON);
        //商品属性集合
        List<ProductCommonProp> productCommonProps = productCommonPropDao.find(commonPropHql, params);
        List<PropName> propNameList = findProp(productCommonProps, productPropImages);
            productDetailsVo.setPropNameList(propNameList);
        //商品图片查询
        String proMediaHql = "from ProductMedia pm where pm.productId=:productId";
        List<ProductMedia> medias = productMediaDao.findProMediaByProId(proMediaHql, id);
        if(medias!=null && !medias.isEmpty()) {
            productDetailsVo.setProductMediaList(medias);
        }

        //商品评论查询
        /*String evaluHql = "select new com.shop.online.vo.EvaluateVo(e.id, e.content, e.productId, e.createTime, e.evaluateLevel, e.nickName, e.pictureLocation, o.skuGroup)" +
                "from Evaluate e left join Order o on e.orderId = o.id where e.productId=:productId";
        List<EvaluateVo> evaluateVoList = evaluateDao.findByproId(evaluHql, id);
        if(evaluateVoList!=null && !evaluateVoList.isEmpty()) {
            productDetailsVo.setEvaluateVoList(evaluateVoList);
        }*/
        return productDetailsVo;
    }

    public List<PropName> findProp(List<ProductCommonProp> productCommonProps, List<ProductPropImage> productPropImages ) throws Exception {
        //按照属性名分类
        Map<String, List<String>> propNames = productCommonProps.stream().collect(Collectors.groupingBy(productCommonProp->productCommonProp.getPropNameId(), Collectors.mapping(productCommonProp->productCommonProp.getPropValueId(), Collectors.toList())));
        List<String> propNameIdList = productCommonProps.stream().map(e->e.getPropNameId()).distinct().collect(Collectors.toList());
        String propNameHql = "from PropName pn where pn.id in (:ids)";
        List<PropName> propNameList = propNameDao.findPropNameByIds(propNameHql, propNameIdList);
        if(!propNameList.isEmpty()) {
            for (PropName propName : propNameList) {
                List<PropValue> propValues = propName.getPropValueList();
                List<PropValue> propValuesNew = new ArrayList<>();
                if(!propValues.isEmpty()) {
                    for(PropValue propValue : propValues) {
                        if(propNames.get(propName.getId()).contains(propValue.getId())) {
                            //属性图片
                            for (ProductPropImage productPropImage : productPropImages) {
                                if (productPropImage.getPropValueId().equals(propValue.getId())) {
                                    propValue.setImgLocation(productPropImage.getImgLocation());
                                }
                            }
                            propValuesNew.add(propValue);
                        }
                    }
                }
                propName.setPropValueList(propValuesNew);
            }
        }
        return propNameList;
    }

    @Resource
    private ProductSkuDao productSkuDao;

    /**
     * 查找指定商品sku属性
     * @param productId
     * @throws Exception
     */
    @Override
    public ProdSkuGroupVo findSkuPropAndProd(String productId) throws Exception {

        String prodPropImageHql = "from ProductPropImage ppi where ppi.productId=:productId and isSkuProp='1'";
        List<ProductPropImage> productPropImages = productPropImageDao.findByProdId(prodPropImageHql, productId);

        //商品基本属性查询(includ销售属性)
        String hql = "from ProductCommonProp pcp where pcp.productId=:productId and pcp.isSku='1' and pcp.status=:status";
        Map<String,Object> params = new HashMap<>();
        params.put("productId", productId);
        //属性状态:启用
        params.put("status", PropNameConstant.PROP_STATE_ON);
        List<ProductCommonProp> productCommonProps = productCommonPropDao.find(hql, params);
        List<PropName> propNameList = findProp(productCommonProps, productPropImages);
        String prodSkuHql = "from ProductSku ps where ps.productId=:productId and ps.status !=:status";
        Map<String, Object> prodSkuParams = new HashMap<>();
        prodSkuParams.put("productId", productId);
        prodSkuParams.put("status", ProductStateConstant.DELETED.getCode());
        List<ProductSku> productSkus = productSkuDao.find(prodSkuHql, prodSkuParams);

        ProdSkuGroupVo prodSkuGroupVo = new ProdSkuGroupVo();
        prodSkuGroupVo.setProductSkuList(productSkus);
        prodSkuGroupVo.setPropNameList(propNameList);
        return prodSkuGroupVo;
    }

    @Override
    public List<PropName> findAllProps(String cataId) {
        String hql_1 = "from PropName pn where pn.cataId=:cataId";
        Map<String, Object> params = new HashMap<>();
        params.put("cataId", cataId);
        List<PropName> propNameList = propNameDao.find(hql_1, params);
        return propNameList;
    }

    /**
     * 获取商品分类树
     * @return
     * @throws Exception
     */
    @Override
    public List<ProductCata> findCategory() throws Exception {

        String hql = "from ProductCata pc where pc.status='1' ";
        List<ProductCata> productCatas = productCataDao.findAllCata(hql);
        if(!productCatas.isEmpty()) {
            return buildTree(productCatas);
        }
        return null;
    }

    @Override
    public PageBean<ProductIndexVo> findProductByCata(String cataId, Integer page, String sort) throws Exception {
        PageBean<ProductIndexVo> pageBean = new PageBean<>();
        pageBean.setCurrentPage(page);
        StringBuilder hqlConn = new StringBuilder();
        hqlConn.append("from Product p, ProductMedia pm where p.id = pm.productId and p.status != :status ");
        if(!StringUtils.isEmpty(cataId)) {
            hqlConn.append("and p.cataId = :cataId ");
        }
        if(StringUtils.isEmpty(sort)) {
            hqlConn.append("order by p.createTime asc");
        }
        else {
            switch (sort) {
                case SortAttributeConstant.PRICE_ASC:
                    hqlConn.append("order by p.priceList asc");
                    break;
                case SortAttributeConstant.PRICE_DESC:
                    hqlConn.append("order by p.priceList desc");
                    break;
                case SortAttributeConstant.TOTAL_SALE_ASC:
                    hqlConn.append("order by p.saleTotalNum asc");
                    break;
                case SortAttributeConstant.TOTAL_SALE_DESC:
                    hqlConn.append("order by p.saleTotalNum desc");
                    break;
                default :
                        hqlConn.append("order by p.createTime asc");
                        break;
            }
        }
        String resultHql = this.hql + hqlConn.toString();;

        Map<String, Object> params = new HashMap<>();
        params.put("status", ProductStateConstant.DELETED.getCode());
        params.put("cataId", cataId);
        Integer count = productDao.count(resultHql, params);
        pageBean.setTotalCount(count!=null? count.intValue() : 0);
        List<ProductIndexVo> productIndexVos = productDao.findProductByCata(resultHql, params, pageBean);
        pageBean.setResultList(productIndexVos);
        return pageBean;
    }

    public List<ProductCata> buildTree(List<ProductCata> productCatas) {
        List<ProductCata> treeNodes = new ArrayList<>();
        List<ProductCata> rootNodes = getRootNodes(productCatas);
        for (ProductCata rootNode : rootNodes) {
            buildChildNodes(rootNode, productCatas);
            treeNodes.add(rootNode);
        }
        return treeNodes;
    }
    public List<ProductCata> getRootNodes(List<ProductCata> productCatas) {
        return productCatas.stream().filter(e -> "0".equals(e.getParentId())).collect(Collectors.toList());
    }
    public void buildChildNodes(ProductCata rootNode, List<ProductCata> productCataList) {
        List<ProductCata> children = getChildNodes(rootNode, productCataList);
        if (!children.isEmpty()) {
            for (ProductCata child : children) {
                buildChildNodes(child, productCataList);
            }
            rootNode.setChildrens(children);
        }
    }
    public List<ProductCata> getChildNodes(ProductCata pnode, List<ProductCata> diskGroupList) {
        List<ProductCata> childNodes = new ArrayList<>();
        for (ProductCata n : diskGroupList) {
            if (pnode.getId().equals(n.getParentId())) {
                childNodes.add(n);
            }
        }
        return childNodes;
    }
}
