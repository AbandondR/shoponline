package com.shop.online.service;

import com.rabbitmq.client.Channel;
import com.shop.online.common.DelayDataBean;
import com.shop.online.common.RefundDataBean;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/11 0011
 */

public interface RabbitMqService {

    void test(String message);

    void sendDelayData(DelayDataBean delayDataBean);
    void refundOrder(RefundDataBean refundDataBean);
    void delayOrderHandle(Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag, Message Message);
    public void refundOrderHandle( Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag, Message message);
}
