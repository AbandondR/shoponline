package com.shop.online.service.impl;

import com.shop.online.common.BaseJunit4Test;
import com.shop.online.util.SysIdApiGenoratorSnowFlake;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/14 0014
 */
public class SnowFlakeGenerateIdTest extends BaseJunit4Test {

    @Resource
    private SysIdApiGenoratorSnowFlake sysIdApiGenoratorSnowFlake;

    @Test
    public void test1() throws Exception {
        for(int i=0; i<2 ;i++) {
            System.out.println(sysIdApiGenoratorSnowFlake.generate("1", 1));
        }
    }
}
