package com.shop.online.service.impl;

import com.shop.online.dao.CartItemDao;
import com.shop.online.dao.UserDao;
import com.shop.online.model.CartItem;
import com.shop.online.model.User;
import com.shop.online.service.UserService;
import com.shop.online.vo.CartItemVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/1/8 0008
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Override
    public String register(User user) throws Exception {
        user.setRegistryTime(new Date());
        user.setLastModify(new Date());
        String id = (String) userDao.save(user);
        return id;
    }

    @Override
    public User findUser(User user) throws Exception {

        if (!StringUtils.isEmpty(user.getId()) || !StringUtils.isEmpty(user.getNickName()) || !StringUtils.isEmpty(user.getTelePhone())) {
            String userHql = "from User user where 1=1 and user.status='1'";
            if (!StringUtils.isEmpty(user.getId())) {
                userHql += "and user.id=:id ";
            }
            if (!StringUtils.isEmpty(user.getNickName())) {
                userHql += "and user.nickName=:nickName ";
            }
            if (!StringUtils.isEmpty(user.getTelePhone())) {
                userHql += "and user.telePhone=:telePhone ";
            }
            userHql += "and user.password=:password";
            User userInfo = userDao.findUser(userHql, user);
            if (userInfo == null) {
                throw new Exception("账户名不存在或密码错误");
            }
            return userInfo;
        } else {
            throw new Exception("查询条件不足");
        }


    }

    @Override
    public void updateUser(User user) throws Exception {
        if (user != null) {
            //String hql = "update User u set u.birthday = :birthday, u.gender=:gender, u.telePhone=:telePhone, u.realName=:realName where u.id=:id";
           userDao.update(user);
        }
    }

    /**
     * 注册号码查重
     * @param telephone
     * @return
     * @throws Exception
     */
    @Override
    public int checkTeleUnique(String telephone) throws Exception {
        String hql = "from User u where u.telePhone=:telephone and u.status='1' ";
        Map<String, Object> params = new HashMap<>();
        params.put("telephone", telephone);
        Integer count = userDao.count(hql, params);
        return count!=null ? count.intValue() : 0;
    }

    @Override
    public String rePass(User user) throws Exception {
        if(user != null) {
            if (!StringUtils.isEmpty(user.getId()) || !StringUtils.isEmpty(user.getNickName())) {
                String hql = "update User u set u.password=:password where 1=1 ";
                if(!StringUtils.isEmpty(user.getId())) {
                    hql += "and u.id=:id";
                }
                if (!StringUtils.isEmpty(user.getNickName())) {
                    hql += "and u.nickName=:nickName ";
                }
                int  count = userDao.updatePassw(hql, user);
                if(count == 1) {
                    return "success";
                }
            }
        }
        return  "failed";
    }

    @Resource
    private CartItemDao cartItemDao;

    @Override
    public List<CartItemVo> queryCartItemList(String userId) throws Exception {
        String hql = "select new com.shop.online.vo.CartItemVo(ci.id,ci.price,ci.skuGroupStr,ci.skuItemCount,ci.skuId,ci.productId, sc.id, ps.imageLocation,ps.stock, p.productName, p.description)" +
                "from CartItem ci, ProductSku ps, Product p, ShopCart sc where sc.customerId=:userId and sc.id=ci.cartId and ci.skuId=ps.id and ci.productId=p.id";
        List<CartItemVo> cartItemVos = cartItemDao.queryCartItemListByUser(hql, userId);
        //计算价格
        for(CartItemVo cartItemVo : cartItemVos) {
            int price = Integer.parseInt(cartItemVo.getPrice());
            int count = Integer.parseInt(cartItemVo.getSkuItemCount());
            cartItemVo.setTotalPrice(String.valueOf(price * count));
        }
        return cartItemVos;
    }

    @Override
    public User findUserById(String id) {
        String hql = "from User u where u.id=:userId and u.status='1' ";
        Map<String, Object> map = new HashMap<>();
        map.put("userId", id);
        return userDao.find(hql, map).get(0);
    }

    @Override
    public int findUserByName(String loginName) {
        String hql = "from User u where u.nickName=:nickName and u.status='1' ";
        Map<String, Object> map = new HashMap<>();
        map.put("nickName", loginName);
        Integer count = userDao.count(hql, map);
        int result = count==null ? 0 : count.intValue();
        return result;
    }

    @Override
    public int updatePassword(String mobile, String password) {
        String hql = "update User u set u.password=:password where u.telePhone=:telePhone";
        Map<String, String> map = new HashMap<>();
        map.put("password", password);
        map.put("telePhone", mobile);
        return userDao.updatePassword2(hql, map);
    }

    @Override
    public int deleteUser(String[] userId) {
        String hql_1 = "update User u set u.status = '0' where u.id=:userId";
        return userDao.deleteUserBatch(hql_1, userId);
    }


}
