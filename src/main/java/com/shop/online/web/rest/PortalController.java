package com.shop.online.web.rest;

import com.shop.online.common.PageBean;
import com.shop.online.model.ProductCata;
import com.shop.online.service.ProductService;
import com.shop.online.vo.ProductIndexVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 网站门户-controller
 * @author YJH
 * @version V1.0 创建时间：2019/3/18 0018
 */
@Controller
@RequestMapping("/")
public class PortalController {

    @Resource
    private ProductService productService;

    /**
     * 主页网站用来显示最新上架商品，以及商品类目，每一部分显示三栏， 点击更多显示，跳转到具体显示页面
     * @param session
     * @param map
     * @return
     */
    @GetMapping("/portal/index")
    public String indexHtml(HttpSession session,Map<String, Object> map) {

        PageBean<ProductIndexVo> newProducts = null;
        List<ProductCata> productCatas = null;
        try {
            newProducts = productService.findNewProduct();
            map.put("newProducts", newProducts);
            productCatas = productService.findCategory();
            if(productCatas != null) {
                map.put("productCatas", productCatas);
                session.setAttribute("productCatas", productCatas);
            }
            return "index_2";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

    }
    @RequestMapping("/")
    public String redirectUrl() {
        return "redirect:/portal/index";
    }
}
