package com.shop.online.service;

import com.shop.online.model.CartItem;
import com.shop.online.model.User;
import com.shop.online.vo.CartItemVo;

import java.util.List;
import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/1/8 0008
 */
public interface UserService {

    String register(User user) throws Exception;

    User findUser(User user) throws Exception;

    void updateUser(User user) throws Exception;

    int checkTeleUnique(String telephone) throws Exception;

    String rePass(User user) throws Exception;

    List<CartItemVo> queryCartItemList(String userId) throws Exception;

    User findUserById(String id);

    int findUserByName(String loginName);

    int updatePassword(String mobile, String password);

    int deleteUser(String[] userId);
}
