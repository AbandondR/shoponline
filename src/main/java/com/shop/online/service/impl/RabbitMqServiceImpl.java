package com.shop.online.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.rabbitmq.client.Channel;
import com.shop.online.common.DelayDataBean;
import com.shop.online.common.RefundDataBean;
import com.shop.online.constant.AlipayConfig;
import com.shop.online.constant.TradeEnum;
import com.shop.online.dao.OrderDao;
import com.shop.online.dao.ProductSkuDao;
import com.shop.online.dao.TradeDao;
import com.shop.online.model.Order;
import com.shop.online.model.Trade;
import com.shop.online.service.OrderService;
import com.shop.online.service.RabbitMqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/11 0011
 */
@Service
@Slf4j
public class RabbitMqServiceImpl implements RabbitMqService {

    @Resource
    private TradeDao tradeDao;

    @Resource
    private ProductSkuDao productSkuDao;

    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderService orderService;

    /**
     * 将生成的订单消息发送到延迟队列
     * @param delayDataBean
     */
    @Override
    public void sendDelayData(DelayDataBean delayDataBean) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        Message message = new Message(JSON.toJSONBytes(delayDataBean), messageProperties);
        delayTemplate.convertAndSend(message);
        System.out.println("已发送订单消息:"+delayDataBean.getTradeNo());
    }
    /*@RabbitListener(queues = "order_dead_queue")
    public void delayTest(String data) {
    }*/

    @RabbitListener(queues = {"order_dead_queue"})
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delayOrderHandle(Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag, Message messageobj) {
        String jsonstr = new String(messageobj.getBody());
        DelayDataBean delayDataBean = JSON.parseObject(jsonstr, DelayDataBean.class);
        String tradeId = delayDataBean.getTradeNo();
        //判定交易状态
        String hql_1 = "from Trade t where t.tradeNum=:tradeNum";
        try {
            Trade trade1 = tradeDao.queryTradeByTradNum(hql_1, tradeId);
            String hql_5 = "from Trade t where t.parentNum=:parentNum";
            List<Trade> trades = tradeDao.queryChildTradeByTradeNum(hql_5, tradeId);
            trades.add(trade1);
            orderService.deleteTradeLock(delayDataBean.getItemToken());
            //订单未支付，取消交易
            for(Trade trade : trades) {
                if (TradeEnum.UNPAID.getCode().equals(trade.getTradeStatus())) {
                    trade.setTradeStatus(TradeEnum.CANCELED.getCode());

                    String hql_2 = "from Order o where o.tradeId=:tradeId";
                    List<Order> orderList = orderDao.queryOrderByTradeId(hql_2, tradeId);
                    //订单状态修改
                    String hql_3 = "update Order o set o.orderStatus=:orderStatus where o.tradeId=:tradeId";
                    int change = orderDao.updateOrderStatusByTradeId(hql_3, TradeEnum.CANCELED.getCode(), trade.getTradeNum());
                    log.info("修改订单状态为“已取消”，{}条记录被修改", change);
                    Map<String, Integer> map_1 = orderList.stream().collect(Collectors.groupingBy(e -> e.getSkuId(), Collectors.summingInt(e -> Integer.parseInt(e.getItemCount()))));
                    //释放库存
                    String hql_4 = "update ProductSku ps set ps.stock=ps.stock+0+:itemCount where ps.id=:skuId";
                    for (String skuId : map_1.keySet()) {
                        int itemCount = map_1.get(skuId).intValue();
                        productSkuDao.updateStockOrSaleNum(hql_4, skuId, itemCount);
                        log.info("修改SKU库存--{}", skuId);
                    }
                    tradeDao.update(trade);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            channel.basicAck(tag, false);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("手动确认异常--{}", e.getMessage());
        }

        System.out.println("订单处理完成----"+delayDataBean.getTradeNo());

    }
    @Resource
    private AmqpTemplate delayTemplate;
    @Override
    public void test(String message) {
        delayTemplate.convertAndSend(message);
        System.out.println("已发送:"+message);
    }

    @Resource
    private AmqpTemplate refundTemplate;
    /**
     * 订单超时退款
     * @param refundDataBean
     */
    @Override
    public void refundOrder(RefundDataBean refundDataBean) {
        refundTemplate.convertAndSend(refundDataBean);
        log.info("订单{}退款处理----发送到退款队列", refundDataBean.getOut_trade_no());
    }

    /**
     * 订单超时，退款处理
     * @param message
     * @param channel
     * @param tag
     */
    @Override
    @RabbitListener(queues = {"refund_queue"})
    public void refundOrderHandle( Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag, Message message) {
        String jsonStr = new String(message.getBody());
        RefundDataBean refundDataBean = JSON.parseObject(jsonStr, RefundDataBean.class);
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
        //设置请求参数
        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();

        try {
            //商户订单号，商户网站订单系统中唯一订单号
            String out_trade_no = new String(refundDataBean.getOut_trade_no().getBytes("ISO-8859-1"), "UTF-8");
            //支付宝交易号
            String trade_no = new String(refundDataBean.getTrade_no().getBytes("ISO-8859-1"), "UTF-8");
            //请二选一设置
            //需要退款的金额，该金额不能大于订单金额，必填
            String refund_amount = new String(refundDataBean.getRefund_amount().getBytes("ISO-8859-1"), "UTF-8");
            //退款的原因说明
            String refund_reason = refundDataBean.getRefund_reason();
            //标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
            //String out_request_no = new String(refundDataBean.getOut_request_no().getBytes("ISO-8859-1"),"UTF-8");
            alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                    + "\"trade_no\":\"" + trade_no + "\","
                    + "\"refund_amount\":\"" + refund_amount + "\","
                    + "\"refund_reason\":\"" + refund_reason + "\"}");

            do {
                String result = alipayClient.execute(alipayRequest).getBody();
                log.info("退款信息{}", result);
            } while(!fastRefundQuery(alipayClient, out_trade_no, trade_no));
            channel.basicAck(tag, false);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean fastRefundQuery(AlipayClient alipayClient, String out_trade_no, String trade_no) throws AlipayApiException {
        //设置请求参数
        AlipayTradeFastpayRefundQueryRequest fastpayRefundQueryRequest = new AlipayTradeFastpayRefundQueryRequest();
        //请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的外部交易号，必填
        String out_request_no =out_trade_no;
        fastpayRefundQueryRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                +"\"trade_no\":\""+ trade_no +"\","
                +"\"out_request_no\":\""+ out_request_no +"\"}");
        //请求
        AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(fastpayRefundQueryRequest);
        if(response.getMsg().equalsIgnoreCase("success") && response.getOutTradeNo()!=null && response.getTotalAmount()!=null) {
            log.info("订单{}退款成功", out_trade_no);
            return true;
        }
        log.info("退款查询:{}", response.getBody());
        return false;
    }

}
