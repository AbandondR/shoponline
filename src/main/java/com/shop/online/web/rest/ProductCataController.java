package com.shop.online.web.rest;

import com.shop.online.model.Brand;
import com.shop.online.model.PropName;
import com.shop.online.service.ProductCataService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/21 0021
 */
@Controller
@RequestMapping("/cata")
public class ProductCataController {
    @Resource
    private ProductCataService productCataService;

    public String findSalePropByCata(String cataId, Map<String, Object> map) throws Exception {
        List<PropName> searchPropSets = productCataService.findSearchPropByCata(cataId);
        List<Brand> brands = productCataService.findBrandByCata(cataId);
        if(brands!=null && !brands.isEmpty()) {
            map.put("brandsByCata", brands);
        }
        if(searchPropSets!=null && !searchPropSets.isEmpty()) {
            map.put("searchPropByCata", searchPropSets);
        }
        return null;
    }

}
