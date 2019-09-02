package com.shop.online.service.impl;

import com.shop.online.common.BaseJunit4Test;
import com.shop.online.dao.ProductSkuDao;
import com.shop.online.dao.UserDao;
import com.shop.online.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/4 0004
 */
@Slf4j
public class UserServiceImplTest extends BaseJunit4Test {

    @Resource
    private UserDao userDao;
    @Test
    @Transactional
    @Rollback(false)
    public void validationLogin() {
        Map<String, Object> map = new HashMap<>(8);
        map.put("userName", "pigpegg");
        map.put("password", "123");
        List<User> users = userDao.find("from User where password = :password and nick_name =:userName ", map);
        log.info("content:{}",users.get(0));
        assert !users.isEmpty();

    }


    @Resource
    private ProductSkuDao productSkuDao;

    @Test
    @Transactional
    @Rollback(false)
    public void register() {
        User user = new User();
        user.setPassword("1234566");
        user.setNickName("test");
        user.setTelePhone("17828042019");
        String id = (String)userDao.save(user);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void updateStock() throws Exception {
        String hql = "update ProductSku ps set ps.stock=ps.stock+0-:itemCount where ps.id=:skuId and ps.stock+0-:itemCount >0";
        Map<String, String> params = new HashMap<>(8);
        //params.put("stock", "20");
        //params.put("skuId", "1");
        //params.put("itemCount", "8");
        //String stock = "10";
        String skuId = "1";
        int itemCount = 18;
        int count = productSkuDao.updateStockOrSaleNum(hql,skuId, itemCount);
    }
}