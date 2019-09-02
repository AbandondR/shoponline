package com.shop.online.web.rest;

import com.shop.online.common.PageBean;
import com.shop.online.common.ResultData;
import com.shop.online.model.Brand;
import com.shop.online.model.Evaluate;
import com.shop.online.model.PropName;
import com.shop.online.service.EvaluateService;
import com.shop.online.service.ProductCataService;
import com.shop.online.service.ProductService;
import com.shop.online.vo.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/21 0021
 */
@Controller
@RequestMapping("/product")
public class ProductController {

    @Resource
    private ProductService productService;

    @Resource
    private ProductCataService productCataService;
    /**
     * 根据分类查找商品
     * @param cataId
     * @return
     */
    @GetMapping("/findproduct")
    public String GetProductByCata(Model model, HttpSession session, @RequestParam("cataId") String cataId, @RequestParam(name="page", defaultValue="1") String currentPage, String sort) {
        Pattern pattern = Pattern.compile("[0-9]{1,9}");
        Integer newCurrPage ;
        //检验前台传过来的分页是否合法
        if(currentPage == null || !pattern.matcher(currentPage).matches()){
            newCurrPage = 1;
        } else {
            newCurrPage = Integer.parseInt(currentPage);
        }
        PageBean<ProductIndexVo> pageBean = new PageBean<>();
        try {
            pageBean = productService.findProductByCata(cataId, newCurrPage, sort);
            model.addAttribute("productPageList", pageBean);
            List<Brand> brands = productCataService.findBrandByCata(cataId);
            List<PropName> searchPropSets = productCataService.findSearchPropByCata(cataId);
            if(brands!=null && !brands.isEmpty()) {
                model.addAttribute("brandsByCata", brands);
            }
            if(searchPropSets!=null && !searchPropSets.isEmpty()) {
                model.addAttribute("searchPropByCata", searchPropSets);
            }
            return "front/search";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

    }

    @GetMapping("/details")
    public String productDetails(Model model, @RequestParam("id") String id) {
        ProductDetailsVo productDetails;
        try {
            productDetails = productService.findProductDetailsById(id);
            List<EvaluateVo> evaluatelist = evaluateService.findEvaluatesByProd(id, 1).getResultList();
            productDetails.setEvaluateVoList(evaluatelist);
            if(productDetails!=null) {
                model.addAttribute("productDetails", productDetails);
            }
            return "front/details";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /*@RequestMapping("/test")
    public String productProp(String productId) {
        return "product/index";
    }
    @RequestMapping(value="/json"*//*, produces = "application/json;charset=utf-8"*//*)
    @ResponseBody
    public ResponseEntity<ResultData> test(HttpServletResponse response) throws Exception{

        String path="C:\\Users\\Administrator\\Desktop\\jquery.sku-master\\jquery.sku-master\\sku\\complex\\data_sku.json";
        String content = ReadFile(path);
        //response.setHeader("Content-type", "application/json;charset=utf-8");
        //URLDecoder.decode(content,"utf-8");
        return ResponseEntity.ok(ResultData.DATA(content));
        //return new String(content.getBytes(),"utf-8");
    }

    public String ReadFile(String Path){
        BufferedReader reader = null;
        String laststr = "";
        try{
            FileInputStream fileInputStream = new FileInputStream(Path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while((tempString = reader.readLine()) != null){
                laststr += tempString;
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return laststr;
    }*/

    @RequestMapping(value="/sku-json", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity<ResultData> findSkuProp(String productId) {
        ProdSkuGroupVo prodSkuGroupVo;
        try {
            prodSkuGroupVo = productService.findSkuPropAndProd(productId);
            return ResponseEntity.ok(ResultData.DATA(prodSkuGroupVo));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ResultData.ERROR(e.getMessage()));
        }
    }

    @Resource
    private EvaluateService evaluateService;

    /**
     * 分页查询商品评价
     * @param productId
     * @param page
     * @return
     */
    @PostMapping("/evaluate")
    @ResponseBody
    public ResponseEntity<ResultData> findEvaluate(String productId, int page) {
        try {
            PageBean pageBean = evaluateService.findEvaluatesByProd(productId, page);
            return ResponseEntity.ok(ResultData.DATA(pageBean));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ResultData.ERROR(e.getMessage()));
        }
    }

    /**
     * 查询分类下的所有属性
     * @param cataId
     */
    @PostMapping("/proplist")
    @ResponseBody
    public PropVo allProps(String cataId){
        List<PropName> propNameList = productService.findAllProps(cataId);
        final List<PropName> otherProps = new ArrayList<>();
        List<PropName> saleProps = propNameList.stream().filter(e->{
            if("1".equals(e.getIsSaleProp())){
                return true;
            }
            else {
                otherProps.add(e);
                return false;
            }
        }).collect(Collectors.toList());
        PropVo propVo = new PropVo();
        propVo.setOtherPropList(otherProps);
        propVo.setSalePropList(saleProps);
        return propVo;
    }

}
