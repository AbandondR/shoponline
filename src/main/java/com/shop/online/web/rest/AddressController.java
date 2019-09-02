package com.shop.online.web.rest;

import com.alibaba.fastjson.JSON;
import com.shop.online.model.Address;
import com.shop.online.model.CountryInfo;
import com.shop.online.model.User;
import com.shop.online.service.AddressService;
import com.shop.online.service.CountryInfoService;
import com.shop.online.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/27 0027
 */
@Controller
@RequestMapping("/address")
@Slf4j
public class AddressController {

    @Resource
    private AddressService addressService;

    @Resource
    private CountryInfoService countryInfoService;

    @GetMapping("/getstreet")
    @ResponseBody
    public String findStreet(String callback, String areaCode, HttpServletRequest request, HttpServletResponse response) {
        //response.setHeader("Content-Type", "application/json;charset=utf8");
        if(StringUtils.isEmpty(areaCode)) {
            //return ResponseEntity.ok(ResultData.ERROR("请选择区"));
        }
        Enumeration names = request.getHeaderNames();
        while(names.hasMoreElements()) {
            String headName = (String) names.nextElement();
            System.out.println(headName + ":" + request.getHeader(headName));
        }
        List<CountryInfo> countryInfoList = countryInfoService.getStreet(areaCode);
        String jsonStr = JSON.toJSONString(countryInfoList);
        String result = callback+"("+jsonStr+")";
        //return ResponseEntity.ok(ResultData.DATA(result));
        return result;
    }
    @Resource
    private UserService userService;


    /**
     * 添加新的收货地址
     * @param address
     * @param session
     * @return
     */
    @PostMapping("/addAddress")
    @ResponseBody
    public String addAddress(Address address, HttpSession session)  {
        String userId = ((User)session.getAttribute("user")).getId();
        User user = userService.findUserById(userId);
        address.setCustomerId(user.getId());
        if(StringUtils.isEmpty(address.getUserName())) {
            address.setUserName(user.getNickName());
        }
        if(StringUtils.isEmpty(address.getTelephone())){
            address.setTelephone(user.getTelePhone());
        }
        try {
            addressService.insertOne(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("{}", address);
        return null;
    }

    /**
     * 返回所有收货地址
     */
    @RequestMapping("/listAddress")
    @ResponseBody
    public void listAddress(HttpSession session, Model model) {
        User user = (User)session.getAttribute("user");
        try {
            List<Address> addressList = addressService.queryAllAddressByUser(user.getId());
            model.addAttribute("addressList", addressList);
            //TODO
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询指定地址
     * @param session
     * @param addressId
     * @return
     */
    @GetMapping("/getAddr")
    @ResponseBody
    public Address getAddress(HttpSession session, String addressId) {
        User user = (User)session.getAttribute("user");
        String userId = user.getId();
        try {
            Address address = addressService.queryOneAddress(addressId,userId);
            if(address != null) {
                return address;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新地址
     * @param address
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping("/updateAddr")
    public String updateAddress(Address address, HttpSession session ,HttpServletRequest request) {
        try {
            //System.out.print(request.getQueryString());
            //InputStream stream = request.getInputStream();
            /*BufferedReader reader = request.getReader();
            String line = null;
            StringBuilder result = new StringBuilder();
            while((line=reader.readLine())!=null) {
                result.append(line);
            }*/
            /*String contenType = request.getContentType();
            System.out.println(contenType);
            String result = request.getQueryString();*/
            /*int size = stream.available();
            byte[] b = new byte[size+1];
            stream.read(b);
            String result = new String(b);*/
            //System.out.print(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        User user = (User)session.getAttribute("user");
        String userId = user.getId();
        address.setCustomerId(userId);
        try {
            int count = addressService.updateAddress(address);
            if(count == 1) {
                return "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    /**
     * 删除指定地址
     * @param addressId
     * @param session
     * @return
     */
    @DeleteMapping("/deleteAddr")
    @ResponseBody
    public String deleteAddress(String addressId, HttpSession session) {
        User user = (User)session.getAttribute("user");
        String userId = user.getId();
        try {
            int count = addressService.deleteAddressById(addressId, userId);
            if(count == 1) {
                return "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    /**
     * 修改默认地址
     */
    @PostMapping("/setDefaultAddr")
    @ResponseBody
    public void updataDefaultAddress(String addressId, HttpSession session) {
        User user = (User)session.getAttribute("user");
        String userId = user.getId();
        try {
            addressService.changeDefaultAddr(addressId, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
