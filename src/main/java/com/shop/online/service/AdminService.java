package com.shop.online.service;

import com.shop.online.common.PageBean;
import com.shop.online.model.Admin;
import com.shop.online.model.User;

import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/5/5 0005
 */
public interface AdminService {
    PageBean<User> getAllUser(int page, String condition) ;

    Admin validationLogin(String userName, String password);
}
