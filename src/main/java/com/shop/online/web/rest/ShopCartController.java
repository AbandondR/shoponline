package com.shop.online.web.rest;

import com.shop.online.dao.CartItemDao;
import com.shop.online.model.ProductSku;
import com.shop.online.model.User;
import com.shop.online.service.ShopCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 购物车
 * @author YJH
 * @version V1.0 创建时间：2019/4/4 0004
 */
@Controller
@RequestMapping("/shopcart")
@Slf4j
public class ShopCartController {

    @Resource
    private ShopCartService shopCartService;

    /**
     * 添加商品到购物车
     * @param productSku 商品sku
     * @param productNum 商品数量
     * @param session
     */
    @PostMapping("/addProduct")
    @ResponseBody
    public String addShop(ProductSku productSku, @RequestParam(defaultValue = "1") String productNum, String skuGroupStr, HttpSession session) {
        User user = (User)session.getAttribute("user");
        String userId = user.getId();
        if(StringUtils.isEmpty(productSku.getId())) {
            return "no_product";
        }
        try {
            int change = shopCartService.addProductAndNum(productSku, skuGroupStr, userId, productNum);
            if(change == 1) {
                return "ok";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "false";

    }

    /**
     * 查询所有的cartItem
     */
    /*@RequestMapping("/allItem")
    public void queryCartItemList(HttpSession session) {
        User user = (User)session.getAttribute("user");
        String userId = user.getId();
    }*/

    /**
     * 删除某个商品
     * @param skuId
     * @param session
     */
    @PostMapping("/deleteItem")
    @ResponseBody
    public String deleteItem(String skuId, HttpSession session) {
        User user = (User)session.getAttribute("user");
        String userId = user.getId();
        try {
            int change = shopCartService.deleteItem(userId, skuId);
            if(change == 1) {
                log.info("删除商品({})成功", skuId);
                return "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "false";
    }

    /**
     * 修改cartItem数量
     * @param skuId
     * @param itemCount
     * @param session
     * @return
     */
    @PostMapping("/updateItemCount")
    @ResponseBody
    public String updateItemCount(String skuId, String itemCount, HttpSession session) {
        User user = (User)session.getAttribute("user");
        String userId = user.getId();
        try {
            return shopCartService.updateItemCount(skuId, itemCount, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
