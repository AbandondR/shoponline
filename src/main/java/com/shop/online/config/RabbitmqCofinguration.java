package com.shop.online.config;

import com.rabbitmq.client.impl.AMQImpl;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/11 0011
 */
@Configuration
public class RabbitmqCofinguration {
    @Value("${rabbit.hosts}")
    private String host;

    @Value("${rabbit.port}")
    private String port;

    @Value("${rabbit.username}")
    private String username;

    @Value("${rabbit.password}")
    private String password;

    @Value("${rabbit.virtualHost}")
    private String virtualHost;

    @Value("${delay.order.exchange}")
    private String delay_exchange;

    @Value("${delay.order.queue}")
    private String delay_queue;

    @Value("${delay.order.routing.key}")
    private String delay_routingKey;

    @Value("${dead.letter.exchange}")
    private String dead_exchange;

    @Value("${dead.letter.queue}")
    private String dead_queue;

    @Value("${dead.letter.routing.key}")
    private String dead_routingkey;

    @Value("${refund.exchange}")
    private String refund_exchange;

    @Value("${refund.queue}")
    private String refund_queue;

    @Value("${refund.routing.key}")
    private String refund_rouingkey;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(this.host);
        connectionFactory.setUsername(this.username);
        connectionFactory.setPassword(this.password);
        connectionFactory.setVirtualHost(this.virtualHost);
        return connectionFactory;
    }

    @Bean(name = "deadTemplate")
    public RabbitTemplate deadTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory());
        rabbitTemplate.setExchange(dead_exchange);
        rabbitTemplate.setRoutingKey(dead_routingkey);
        rabbitTemplate.setDefaultReceiveQueue(dead_queue);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean(name = "delayTemplate")
    public RabbitTemplate delayTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory());
        rabbitTemplate.setExchange(delay_exchange);
        rabbitTemplate.setRoutingKey(delay_routingKey);
        rabbitTemplate.setDefaultReceiveQueue(delay_queue);
        //rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean(name = "refundTemplate")
    public RabbitTemplate refundTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory());
        rabbitTemplate.setExchange(refund_exchange);
        rabbitTemplate.setRoutingKey(refund_rouingkey);
        rabbitTemplate.setDefaultReceiveQueue(refund_queue);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }



    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public DirectExchange delayExchange() {
        DirectExchange exchange = new DirectExchange(delay_exchange, false, false);
        return exchange;
    }


    @Bean
    DirectExchange deadExchange() {
        DirectExchange exchange = new DirectExchange(dead_exchange, false, false);
        return exchange;
    }
    @Bean
    public Queue delayQueue() {
        //声明订单延迟队列
        Map<String, Object> params = new HashMap<>(8);
        //消息过期时间30s
        params.put("x-message-ttl", 5*60*1000);
        //指定死信路由交换
        params.put("x-dead-letter-exchange", dead_exchange);
        params.put("x-dead-letter-routing-key", dead_routingkey);
        Queue queue =new Queue(delay_queue, false, false, false, params);
        return queue;
    }

    @Bean
    public Queue deadQueue() {
        Queue queue = QueueBuilder.nonDurable(dead_queue).exclusive().autoDelete().build();
        return queue;
    }
    @Bean
    public Binding binding_1() {
        Binding binding = BindingBuilder.bind(deadQueue()).to(deadExchange()).with(dead_routingkey);
        return binding;
    }
    @Bean
    public Binding binding_2() {
        Binding binding = BindingBuilder.bind(delayQueue()).to(delayExchange()).with(delay_routingKey);
        return binding;
    }

    @Bean
    public Queue refundQueue() {
        Queue queue = new Queue(refund_queue, false, true, false);
        return queue;
    }

    @Bean
    public DirectExchange refundExchange() {
        DirectExchange exchange = new DirectExchange(refund_exchange, false, false);
        return exchange;
    }

    @Bean
    public Binding binding_3() {
        Binding binding = BindingBuilder.bind(refundQueue()).to(refundExchange()).with(refund_rouingkey);
        return binding;
    }
}
