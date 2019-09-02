package com.shop.online.dao;

import com.shop.online.model.User;

import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/1/8 0008
 */
public interface UserDao extends BaseDao<User>{

    User findUser(String hql, User user) throws Exception;

    int updateUser(String hql, User user) throws Exception;

    int updatePassw(String hql, User user) throws Exception;

    int updatePassword2(String hql, Map<String,String> map);

    Long countUser_admin(String hql_2, Map<String,Object> params);

    int deleteUserBatch(String hql_1, String[] userId);
}
