package com.shop.online.service.impl;

import com.shop.online.common.BaseJunit4Test;
import com.shop.online.common.DelayDataBean;
import com.shop.online.dao.TradeLockDao;
import com.shop.online.model.TradeLock;
import com.shop.online.service.RabbitMqService;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/12 0012
 */
public class OrderServiceTest extends BaseJunit4Test {
    @Resource
    private TradeLockDao tradeLockDao;

    @Test
    @Transactional
    @Rollback(false)
    public void addTradeLock() {
        String itemToken = "123";
        TradeLock tradeLock = new TradeLock();
        tradeLock.setTradeToken(itemToken);
        boolean repeated = false;
        try {
            Integer identifier = (Integer) tradeLockDao.save(tradeLock);
            System.out.println(identifier.intValue());
        }catch(Exception e) {
            e.printStackTrace();
            repeated = true;
        }
        System.out.println(repeated);
    }
    @Resource
    private RabbitMqService rabbitMqService;

    @Test
    @Transactional
    @Rollback(false)
    public void testMq() {
        DelayDataBean delayDataBean = new DelayDataBean();
        delayDataBean.setTradeNo("222313423223");
        delayDataBean.setSkuIds(new String[]{"123", "124"});
        rabbitMqService.sendDelayData(delayDataBean);
    }
}
