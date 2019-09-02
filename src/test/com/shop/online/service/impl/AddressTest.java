package com.shop.online.service.impl;

import com.shop.online.common.BaseJunit4Test;
import com.shop.online.dao.AddressDao;
import com.shop.online.model.Address;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/28 0028
 */
public class AddressTest  extends BaseJunit4Test {

    @Resource
    private AddressDao addressDao;


    @Test
    @Transactional
    @Rollback(true)
    public void test() {
        //List<Address> addresses = addressDao.find("from Address a where a.customerId = '1'");
        String hql = "select count(*) from Address a where a.customerId='1' for update";
        addressDao.find(hql);
        System.out.println("test");
    }

    @Test
    public void test2( ) {
        String a = null;
        a += "12323";
        System.out.println(a);
    }
}
