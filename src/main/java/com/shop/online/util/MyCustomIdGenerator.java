package com.shop.online.util;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Properties;

/**
 * 自定义主键生成策略
 * @author YJH
 * @version V1.0 创建时间：2019/3/1 0001
 */

@Component
public class MyCustomIdGenerator implements IdentifierGenerator, Configurable {
    private String prefix;
    private String batchNum;
    private SysIdApiGenoratorSnowFlake dcpSysIdApiGenoratorSnowFlake;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        try {
            if(dcpSysIdApiGenoratorSnowFlake == null) {
                dcpSysIdApiGenoratorSnowFlake = new SysIdApiGenoratorSnowFlake();
            }
            return dcpSysIdApiGenoratorSnowFlake.generate(prefix, Integer.parseInt(batchNum));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        this.prefix = params.getProperty("prefix");
        this.batchNum = params.getProperty("batchNum");
    }
}
