package com.shop.online.service.impl;

import com.shop.online.common.PageBean;
import com.shop.online.dao.AdminDao;
import com.shop.online.dao.UserDao;
import com.shop.online.model.Admin;
import com.shop.online.model.User;
import com.shop.online.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/5/5 0005
 */
@Service
public class AdminServiceImpl implements AdminService {

    public static final int PAGE_SIZE = 10;
    @Resource
    private UserDao userDao;


    @Override
    public PageBean<User> getAllUser(int page, String condition) {
        PageBean<User> pageBean = new PageBean();
        pageBean.setPageSize(PAGE_SIZE);
        pageBean.setCurrentPage(page);
        String hql_1 = "from User u where 1=1 and u.status='1' ";
        String hql_2 = "select count(1) from User u where 1=1 and u.status='1' ";
        Map<String, Object> params = new HashMap<>();
        if(!StringUtils.isEmpty(condition)) {
            hql_1 += "and u.nickName=:nickName";
            hql_2 += "and u.nickName=:nickName";
            params.put("nickName", condition);
        }
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<User> users = userDao.find(hql_1, params, page, PAGE_SIZE);
        users.stream().filter(e->{
            try {
                //e.setRegistryTime( new Date(simpleDateFormat.format(e.getRegistryTime())));
                if(e.getGender().equals("1")) {
                    e.setGender("男");
                }
                if(e.getGender().equals("2")) {
                    e.setGender("女");
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return true;
        }).collect(Collectors.toList());
        Long count_obj = userDao.countUser_admin(hql_2, params);
        int count = count_obj==null ? 0 : count_obj.intValue();
        pageBean.setResultList(users);
        pageBean.setTotalCount(count);
        return pageBean;
    }

    @Resource
    private AdminDao adminDao;
    @Override
    public Admin validationLogin(String userName, String password) {
        String hql = "from Admin a where a.loginName=:loginName and password=:password";
        Map<String, Object> params = new HashMap<>();
        params.put("loginName", userName);
        params.put("password", password);
        List<Admin> admins = adminDao.find(hql, params);
        if(admins.isEmpty()) {
           return null;
        }
        return admins.get(0);
    }
}
