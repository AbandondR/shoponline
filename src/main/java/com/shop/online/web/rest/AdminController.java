package com.shop.online.web.rest;

import com.shop.online.common.PageBean;
import com.shop.online.model.Admin;
import com.shop.online.model.Order;
import com.shop.online.model.ProductCata;
import com.shop.online.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/5/4 0004
 */
@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {


   /* @Resource
    private AdminService adminService;
    *//**
     * 管理员首页
     *//*
    @GetMapping("/index")
    public String indexHtml() {
        return "back/admin/admin-index";
    }

    *//**
     * 用户管理界面
     * @return
     *//*
    @GetMapping("/user-mana")
    public String userManaHtml() {
        return "back/admin/user-list";
    }

    *//**
     * 获取所有用户
     * @return
     *//*
    @PostMapping("/user-list")
    @ResponseBody
    public PageBean<User> getUsers(int page, int rows) {
        PageBean pageBean = null;
        return adminService.getAllUser(page, rows);

    *//**
     * 修改用户状态
     *//*
    public void changeUserStatus(String statuValue, String userId) {

    }*/
    @Resource
    private OrderService orderService;
    /**
     * 订单管理
     */
    @GetMapping("/orderList/{currPage}")
    public String orderList(@PathVariable Integer currPage, String condition, String state, Model model){

        PageBean<Order> orderPageBean = orderService.queryOrderByPage(currPage,condition, state);
        model.addAttribute("orderPageBean",orderPageBean);
        model.addAttribute("condition",condition);
        model.addAttribute("state",state);
        return "back/admin/orderList";
    }

    @GetMapping("/orderDetail/{id}")
    public String orderDetailHtml(@PathVariable("id") String orderNum, Model model) {
        //根据id查询到这条订单
        Order order = null;
        try {
            order = orderService.queryOrderDetails_admin(orderNum);
            model.addAttribute("order",order);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "back/admin/orderDetail";
    }

    @PostMapping("/order/updateStatus")
    @ResponseBody
    public void updateOrderStatus(String orderNum, String state) {

        try {
            orderService.updateOrderStatusById_admin(orderNum, state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Resource
    private AdminService adminService;
    /**
     * 获取用户列表
     * @return
     */
    @GetMapping("/userList/{currpage}")
    public String getAllUser(@PathVariable("currpage") int currpage, String condition, Model model) {
        PageBean pageBean = null;
        pageBean = adminService.getAllUser(currpage, condition);
        model.addAttribute("userPageBean",pageBean);
        model.addAttribute("condition",condition);
        return "back/admin/userList";
    }

    @Resource
    private UserService userService;

    /**
     * 删除用户
     * @param userId
     * @return
     */
    @GetMapping("/user/delete/{userId}")
    @ResponseBody
    public String deleteUser(@PathVariable("userId") String[] userId) {

        int deleteCount = userService.deleteUser(userId);
        if(deleteCount>0) {
            log.info("删除用户成功");
            return "success";
        }
        return "error";
    }

    @Resource
    private ProductCataService productCataService;

    @Resource
    private ProductService productService;
    /**
     * 商品分类列表
     */
    @GetMapping("/kindList/{currpage}")
    public String productCataList(Model model, @PathVariable int currpage, String idCondition, String nameCondition){
        try {
            //List<ProductCata> cataList = productService.findCategory();
            if(!StringUtils.isEmpty(nameCondition)) {
                nameCondition = new String(nameCondition.getBytes("ISO-8859-1"), "utf-8");
            }
            PageBean<ProductCata> cataList = productCataService.getAllCategory_admin(currpage, idCondition, nameCondition);
            model.addAttribute("cataPageBean", cataList);
            model.addAttribute("idCondition", idCondition);
            model.addAttribute("nameCondition", nameCondition);
            if(!StringUtils.isEmpty(idCondition)) {
                ProductCata productCata = productCataService.findCataById(idCondition);
                model.addAttribute("conditionCataName", productCata.getCataName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "back/admin/kindList";
    }

    /**
     * 修改分类名称
     */
    @PostMapping("/kind/edit")
    @ResponseBody
    public String editCataName(String cataId, String cataName) {
        if(StringUtils.isEmpty(cataName) || StringUtils.isEmpty(cataId)) {
            return "failed";
        }
        /**
         * "repeat":"已经存在该分类名称", "error":系统错误, "success":成功
         */
        String status = productCataService.updateCataName(cataId, cataName);
        return status;
    }

    /**
     * 更新分类状态（禁用、启用）
     * @param id
     * @param status
     * @return
     */
    @PostMapping("/kind/updateStatus")
    @ResponseBody
    public String updateCataStatus(String id, String status) {
        return productCataService.updateCataStatus(id, status);
    }

    @GetMapping("/kind/tree")
    @ResponseBody
    public List<ProductCata> getCataTree(String id) {
        try {
            return productCataService.findCategoryByPid(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @PostMapping("/kind/add")
    @ResponseBody
    public String addProductCata(ProductCata productCata) {

        String status = null;
        try {
            status = productCataService.insertOne(productCata);
        } catch (Exception e) {
            status = e.getMessage();
            e.printStackTrace();
        }
        return status;
    }

    @GetMapping("/product/add")
    public String addProduct() {
        return "back/admin/addProduct";
    }


    @PostMapping("/loginHandler")
    public String AdminLogin(String userName, String password, HttpSession session){
        String msg = null;
        if(org.springframework.util.StringUtils.isEmpty(userName)|| org.springframework.util.StringUtils.isEmpty(password)) {
            msg = "参数错误";
            session.setAttribute("adminLoginMsg", msg);
            return "redirect:/admin/login";
        }
        Admin admin = adminService.validationLogin(userName, password);
        if(admin!=null) {
            session.setAttribute("admin", admin);
            return "redirect:/admin/userList/1";
        }
        msg = "登录名或密码错误";
        session.setAttribute("adminLoginMsg", msg);
        return "redirect:/admin/login";
    }
    @GetMapping("/login")
    public String adminLoginHtml() {
        return "back/admin/login-admin";
    }

    @GetMapping("/admin/logout")
    @ResponseBody
    public String adminLogOut(HttpSession session) {
        session.removeAttribute("admin");
        //session.removeAttribute("adminLoginMsg");
        return null;
    }

}
