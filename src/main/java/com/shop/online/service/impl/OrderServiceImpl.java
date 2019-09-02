package com.shop.online.service.impl;

import com.shop.online.common.PageBean;
import com.shop.online.common.RefundDataBean;
import com.shop.online.constant.AlipayConfig;
import com.shop.online.constant.TradeEnum;
import com.shop.online.dao.*;
import com.shop.online.model.*;
import com.shop.online.service.OrderService;
import com.shop.online.service.RabbitMqService;
import com.shop.online.util.AddressUtil;
import com.shop.online.util.ObjectConvertUtil;
import com.shop.online.util.SysIdApiGenoratorSnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/5 0005
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private ShopCartDao shopCartDao;

    @Resource
    private AddressDao addressDao;

    @Resource
    private CartItemDao cartItemDao;

    @Resource
    private ProductSkuDao productSkuDao;

    @Resource
    private OrderDao orderDao;

    @Resource
    SysIdApiGenoratorSnowFlake sysIdApiGenoratorSnowFlake;

    /**
     * 添加订单
     * @param skuIds
     * @param addressId
     * @param user
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> addOrder(String[] skuIds, String addressId, User user) throws Exception {
        Trade trade = new Trade();
        //查找当前用户的购物车
        String shopCartHql = "from ShopCart sc where sc.customerId=:userId";
        ShopCart shopCart = shopCartDao.queryOne(shopCartHql, user.getId());
        boolean isExclusiveTrade = false;
        //是否是单个订单对应单个交易
        if(skuIds.length == 1) {
            isExclusiveTrade = true;
        }
        //收货地址
        String addressHql = "from Address a where a.id=:addressId and a.customerId=:userId";
        Address address = addressDao.queryAddressById(addressHql, addressId, user.getId());
        //从购物车中查找需要购买的商品
        String cartIemHql = "from CartItem ci where ci.cartId=:cartId and skuId in (:skuIds)";
        List<CartItem> cartItemList = cartItemDao.queryItemsBySkuIdsAndcartId(cartIemHql, shopCart.getId(), skuIds);
        Map<String, Integer> map = cartItemList.stream().collect(Collectors.groupingBy(e->e.getSkuId(), Collectors.summingInt(e->Integer.parseInt(e.getSkuItemCount()))));
        Map<String, List<CartItem>> map2 = cartItemList.stream().collect(Collectors.groupingBy(e->e.getSkuId(), Collectors.toList()));
        //生成交易编号
        String tradeId = String.valueOf(UUID.randomUUID());
        String tradeNum = sysIdApiGenoratorSnowFlake.generate("5",1);
        log.info("交易编号：{}", tradeNum);
        //减库存
        for(int i=0; i < skuIds.length; i++) {

            String querySkuHql = "from ProductSku ps where ps.id=:skuId";
            ProductSku productSku = productSkuDao.QueryOneById(querySkuHql, skuIds[i]);
            String hql = "update ProductSku ps set ps.stock=ps.stock+0-:itemCount where ps.id=:skuId and ps.stock+0-:itemCount >0";
            int itemCount = map.get(skuIds[i]);
            int change = productSkuDao.updateStockOrSaleNum(hql, skuIds[i], itemCount);
            if(change < 0) {
                throw new Exception("leak_stock");
            }
            Order order = new Order();
            BigDecimal price = new BigDecimal(productSku.getPrice());
            BigDecimal amount = price.multiply(new BigDecimal(itemCount)).setScale(2,BigDecimal.ROUND_HALF_UP);
            order.setItemCount(String.valueOf(itemCount));
            order.setPrice(productSku.getPrice());
            order.setItemCount(String.valueOf(map.get(skuIds[i])));
            order.setAmount(amount.toPlainString());
            order.setDescription(productSku.getDescription());
            order.setImageLocation(productSku.getImageLocation());
            order.setActualPaid(amount.toPlainString());

            //未付款
            order.setOrderStatus(TradeEnum.UNPAID.getCode());
            order.setSkuGroup(map2.get(skuIds[i]).get(0).getSkuGroupStr());
            order.setProductId(productSku.getProductId());
            order.setSkuId((productSku.getId()));
            String orderNum = sysIdApiGenoratorSnowFlake.generate("1", 1);
            log.info("订单号{}",sysIdApiGenoratorSnowFlake.generate("1", 1));
            order.setOrderNum(orderNum);
            order.setId(String.valueOf(UUID.randomUUID()));
            order.setCustomerId(user.getId());
            order.setTradeId(tradeNum);
            order.setCreateTime(new Date());
            order.setIsExclusiveTrade(isExclusiveTrade?"1":"0");
            order.setReceiver(address.getUserName());
            orderDao.save(order);
        }
        //总金额
        BigDecimal totalPrice = new BigDecimal(BigInteger.ZERO);
        //总数量
        int itemNum = 0;
        for(CartItem cartItem : cartItemList) {
            BigDecimal price = new BigDecimal(cartItem.getPrice());
            BigDecimal temp = price.multiply(BigDecimal.valueOf(Integer.parseInt(cartItem.getSkuItemCount())));
            totalPrice = totalPrice.add(temp);
            itemNum += Integer.parseInt(cartItem.getSkuItemCount());
        }

        trade.setAmount(totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        trade.setActualPaid(totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        trade.setCustomerId(user.getId());
        trade.setCustomerName(address.getUserName());
        // temp = new HashMap<>(8);
        Map<String, Object> temp = ObjectConvertUtil.Obj2Map(address);
        trade.setAddressGroup(AddressUtil.combineAddress(temp));
        trade.setExpressFee("0.00");
        //全部为在线支付
        trade.setTradeType(TradeEnum.ONLINE_PAY.getCode());
        trade.setCustomerTel(address.getTelephone());
        trade.setExpressType("平邮");
        trade.setDeleted("0");
        trade.setHasExpressFee("0");
        trade.setId(tradeId);
        trade.setHasParent("0");
        //在线支付
        trade.setPaidType(TradeEnum.ONLINE_PAY.getCode());
        trade.setTradeNum(tradeNum);
        trade.setTradeCreateTime(new Date());
        //未支付
        trade.setTradeStatus(TradeEnum.UNPAID.getCode());
        String tradeToken = cartItemList.stream().map(e->e.getId()).collect(Collectors.joining("_"));
        trade.setTradeToken(tradeToken);
        //交易有效
        trade.setIsValid(TradeEnum.VALID.getCode());
        tradeDao.save(trade);

        //删除购物车
        for(CartItem cartItem : cartItemList) {
            cartItemDao.delete(cartItem);
        }
        Map<String, String> data = new HashMap<>(8);
        data.put("totalAmount", totalPrice.toPlainString());
        data.put("tradeId", trade.getTradeNum());
        data.put("description", "测试");
        data.put("orderName", "测试");
        return data;
    }
    @Resource
    private TradeDao tradeDao;
    @Override
    public int countTradeByItemToken(String itemToken, String userId) throws Exception {
        String tradeHql = "from Trade t where t.customerId=:userId and t.tradeToken=:tradeToken and t.tradeType=:tradeType and t.tradeStatus=:tradeStatus";
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("tradeToken", itemToken);
        params.put("tradeType", TradeEnum.ONLINE_PAY.getCode());
        params.put("tradeStatus", TradeEnum.UNPAID.getCode());
        Integer temp = tradeDao.count(tradeHql, params);
        int count = temp != null ? temp.intValue() : 0;
        return count;
    }

    @Override
    public Trade queryTradeById(String outTradeNo) throws Exception {
        if(!StringUtils.isEmpty(outTradeNo)) {
            String hql = "from Trade t where t.tradeNum = :tradeNum";
            Trade trade = tradeDao.queryTradeByTradNum(hql, outTradeNo);
            return trade;
        }
        return null;
    }

    @Override
    public void updateTrade(Trade trade) throws Exception {
        tradeDao.update(trade);
    }

    @Resource
    private TradeLockDao tradeLockDao;
    @Override
    public Integer addTradeLock(String itemToken) throws Exception {
        TradeLock tradeLock = new TradeLock();
        tradeLock.setTradeToken(itemToken);
        return (Integer)tradeLockDao.save(tradeLock);
    }

    @Override
    public int deleteTradeLock(String itemToken) {
        String hql = "delete from TradeLock tl where tl.tradeToken=:tradeToken";
        return tradeLockDao.deleteOne(hql, itemToken);
    }

    @Override
    public List<Order> queryOrderByTradeId( String id) {
        String hql_2 = "from Order o where o.tradeId=:tradeId";
        return orderDao.queryOrderByTradeId(hql_2, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateOrderStatusByTradeId(String code, String id) {
        String hql_3 = "update Order o set o.orderStatus=:orderStatus where o.tradeId=:tradeId";
        int change = orderDao.updateOrderStatusByTradeId(hql_3, code, id);
        return change;
    }

    /**
     * 根据用户查找订单
     * @param currPage
     * @param state
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public PageBean<Order> queryOrderByUser(int currPage, String state, String userId) throws Exception {

        PageBean pageBean = new PageBean();
        pageBean.setCurrentPage(currPage);
        pageBean.setPageSize(5);
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        String hql_1 = "from Order o where o.customerId=:userId ";
        String hql_2 = "select count(1) from Order o where o.customerId=:userId ";
        if(!StringUtils.isEmpty(state)) {
            hql_1 += "and o.orderStatus=: orderStatus";
            hql_2 += "and o.orderStatus=: orderStatus";
            params.put("orderStatus", state);
        }
        hql_1 += " order by o.createTime desc";
        List<Order> orderList = orderDao.queryOrderPageByUser(hql_1, params, currPage, pageBean.getPageSize());
        List<Order> orderList1 = orderList.stream().filter(e->{
            e.setOrderStatus(TradeEnum.getDescByCode(e.getOrderStatus()));
            return true;
        }).collect(Collectors.toList());
        pageBean.setResultList(orderList1);
        Long count = orderDao.countOrderByUser(hql_2, params);
        pageBean.setTotalCount(count==null? 0 : count.intValue());
        return pageBean;
    }

    /**
     * 统计个状态下的订单数量
     * @param userId
     */
    @Override
    public Map<String, Long> countStateByUser(String userId) {
        String hql_1 = "from Order o where o.customerId=:userId";
        List<Order> orderList = orderDao.countStateByUser(hql_1, userId);
        Map<String, Long> map_1 = orderList.stream().collect(Collectors.groupingBy(e->TradeEnum.getDescByCode(e.getOrderStatus()), Collectors.counting()));
        return map_1;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, String> queryOrderForPay(String orderNum, String userId) throws Exception {
        String hql_1 = "from Order o where o.orderNum=:orderNum and o.customerId=:userId ";
        Order order = orderDao.queryOrderByOrderNum(hql_1, orderNum, userId);
        String hql_2 = "from Trade t where t.tradeNum=:tradeNum";
        Trade trade1 = tradeDao.queryTradeByTradNum(hql_2, order.getTradeId());
        Map<String, String> data = new HashMap<>(8);
        if(trade1.getTradeStatus().equals(TradeEnum.UNPAID.getCode())
                || trade1.getIsValid().equals(TradeEnum.INVALID.getCode())) {
            //判断订单是否是独占一个交易
            boolean exclusiceTrade = order.getIsExclusiveTrade() .equals("1") ? true : false;
            data.put("totalAmount", order.getAmount());
            //TODO 暂时写死
            data.put("description", "测试");
            data.put("orderName", "测试");
            if (exclusiceTrade) {
                data.put("tradeId", order.getTradeId());
            }
            else {
                //将之前的交易设置为无效
                if(trade1.getIsValid().equals(TradeEnum.VALID.getCode())) {
                    trade1.setIsValid(TradeEnum.INVALID.getCode());
                    tradeDao.update(trade1);
                }
                Trade trade = new Trade();
                String tradeId = String.valueOf(UUID.randomUUID());
                String tradeNum = sysIdApiGenoratorSnowFlake.generate("5",1);
                String hasParent = "1";
                //是交易号，并不是交易ID
                String parentNum = order.getTradeId();
                trade.setHasParent(hasParent);
                trade.setTradeNum(tradeNum);
                trade.setId(tradeId);
                trade.setParentNum(parentNum);
                trade.setTradeStatus(TradeEnum.UNPAID.getCode());
                trade.setTradeCreateTime(new Date());
                trade.setAmount(order.getAmount());
                trade.setActualPaid(order.getActualPaid());
                trade.setIsValid(TradeEnum.VALID.getCode());
                trade.setCustomerId(userId);
                tradeDao.save(trade);
                //设置为独占
                order.setIsExclusiveTrade("1");
                order.setTradeId(tradeNum);
                orderDao.update(order);
                data.put("tradeId", tradeNum);
            }
        }
        else {
            throw new Exception("订单状态条件不吻合");
        }
        //订单是否超时取消
        return data;
    }

    @Override
    public int updateOrderStatusById(String orderId, String userId, String state, String tradeId) throws Exception {

        //查询交易
         String hql_3 = "from Trade t where t.tradeNum=:tradeNum and t.customerId=:userId";
         Map<String, String> params1 = new HashMap<>();
         params1.put("tradeNum", tradeId);
         params1.put("userId", userId);
         Trade trade = tradeDao.queryTradeByTradeNumAndUser(hql_3, params1);
        //查询订单
        String hql_1 = "from Order o where o.orderNum=:orderNum and o.customerId=:userId";
        Order order = orderDao.queryOrderByOrderNum(hql_1, orderId, userId);
        String orderState = order.getOrderStatus();
        String statusCode = TradeEnum.getCodeByDesc(state);
        if(StringUtils.isEmpty(statusCode)) {
            throw new Exception("非法的订单状态");
        }
        order.setOrderStatus(statusCode);
        if(order==null) {
            throw new Exception("非法请求");
        }
        String hql_2 = "update Order o set o.orderStatus=:orderStatus where o.orderNum=:orderNum and o.customerId=:userId";
        Map<String, String> params2 = new HashMap<>();
        params2.put("orderStatus", statusCode);
        params2.put("orderNum", orderId);
        params2.put("userId", userId);
        int change = 0;
        switch (statusCode) {
            case "00000":
                //必须是未付款的订单才能取消
                if (trade.getTradeStatus().equals(TradeEnum.FINISHED.getCode()) && trade.getIsValid().equals(TradeEnum.VALID.getCode())) {
                    throw new Exception("该订单已完成付款，请刷新页面");
                }
                else{
                    //trade.setIsValid(TradeEnum.INVALID.getCode());
                    if(order.getIsExclusiveTrade().equals("1")) {
                        trade.setTradeStatus(TradeEnum.CANCELED.getCode());
                        tradeDao.update(trade);
                    }

                    orderDao.updateOrderStatusById(hql_2, params2);
                }
                break;
            case "0011":
                //必须是已支付的订单才能变为已发货的状态
                if (orderState.equals("0001")) {
                    change = orderDao.updateOrderStatusById(hql_2, params2);
                }
                break;
            case "0111":
                //必须是已发货的订单才能编程已收货的状态
                if (orderState.equals("0011")) {
                    change = orderDao.updateOrderStatusById(hql_2, params2);
                }
                break;
            case "1111":
                //必须是已评价的订单才能变为已完成的状态
                if (orderState.equals("0111")) {
                    change = orderDao.updateOrderStatusById(hql_2, params2);
                }
                break;
        }
        return change;
    }

    @Resource
    private RabbitMqService rabbitMqService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String aliPayCallBack(Map<String, String[]> requestParams) throws Exception {

        //商户订单号
        String outTradeNo = requestParams.get("out_trade_no")[0];
        //支付宝交易号
        String tradeNo = requestParams.get("trade_no")[0];
        //交易状态
        String tradeStatus = requestParams.get("trade_status")[0];
        //开发者appid
        String appId = requestParams.get("app_id")[0];
        //卖家支付宝用户号
        String sellerId = requestParams.get("seller_id")[0];
        //订单金额
        String totalAmount = requestParams.get("total_amount")[0];
        //付款时间
        String gmtPayment = requestParams.get("gmt_payment")[0];
        String hql_1 = "from Trade t where t.tradeNum = :tradeNum";
        Trade trade = tradeDao.queryTradeByTradNum(hql_1, outTradeNo);
        if (trade.getAmount().equals(totalAmount) && AlipayConfig.app_id.equals(appId) && AlipayConfig.seller_id.equals(sellerId)) {
            if (TradeEnum.UNPAID.getCode().equals(trade.getTradeStatus())
                    && TradeEnum.VALID.getCode().equals(trade.getIsValid())) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    trade.setPayFinishTime(sdf.parse(gmtPayment));
                    trade.setPayNum(tradeNo);
                    //当交易状态为未支付，修改交易状态为交易完成
                    trade.setTradeStatus(TradeEnum.FINISHED.getCode());
                    tradeDao.update(trade);
                    //修改订单状态
                    String hql_3 = "update Order o set o.orderStatus=:orderStatus where o.tradeId=:tradeId";
                    int change = orderDao.updateOrderStatusByTradeId(hql_3, TradeEnum.UNSHIPPED.getCode(), trade.getTradeNum());
                    log.info("修改订单状态为“待发货”，{}条记录被修改", change);
                    //TODO 修改销售量
                    String hql_4 = "from Order o where o.tradeId=:tradeId";
                    List<Order> orderList = orderDao.queryOrderByTradeId(hql_4, outTradeNo);
                    Map<String, Integer> map_1 = orderList.stream().collect(Collectors.groupingBy(e -> e.getSkuId(), Collectors.summingInt(e -> Integer.parseInt(e.getItemCount()))));
                    String hql_5 = "update ProductSku ps set ps.saleNum=ps.saleNum+0+:itemCount where ps.id=:skuId";
                    for (String skuId : map_1.keySet()) {
                        int itemCount = map_1.get(skuId).intValue();
                        productSkuDao.updateStockOrSaleNum(hql_5, skuId, itemCount);
                        log.info("修改SKU销量--{}", skuId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //如果此时订单已取消或交易无效
            if (TradeEnum.CANCELED.getCode().equals(trade.getTradeStatus())
                    || TradeEnum.INVALID.getCode().equals(trade.getIsValid())) {
                //退款
                RefundDataBean refundDataBean = new RefundDataBean();
                refundDataBean.setOut_request_no(outTradeNo);
                refundDataBean.setOut_trade_no(outTradeNo);
                refundDataBean.setRefund_amount(totalAmount);
                refundDataBean.setRefund_reason("订单超时已取消或交易无效");
                refundDataBean.setTrade_no(tradeNo);
                rabbitMqService.refundOrder(refundDataBean);
                log.info("交易号:{}退款", outTradeNo);
            }
            return "success";
        }
        return "failure";
    }

    @Override
    public Order queryOrderByOrderNum(String orderId, String userId) throws Exception {
        String hql_1 = "from Order o where o.orderNum=:orderNum and o.customerId=:userId";
        return orderDao.queryOrderByOrderNum(hql_1, orderId, userId);
    }

    @Override
    public String isTakeGoodes(String orderId, String userId) {
        String hql_1 = "from Order o where o.orderNum=:orderNum and o.customerId=:userId";
        Order order = orderDao.queryOrderByOrderNum(hql_1, orderId, userId);
        if(order.getOrderStatus().equals(TradeEnum.UNCOMMENTED.getCode())) {
            return "success";
        }
        return "failure";
    }

    @Override
    public Order queryOrderDetails(String orderNum, String id) throws Exception{
        //查询订单
        String hql_1 = "from Order o where o.orderNum=:orderNum and o.customerId=:userId";
        Order order = orderDao.queryOrderByOrderNum(hql_1, orderNum, id);
        String hql_2 = "from Trade t where t.tradeNum=:tradeNum";
        Trade trade = tradeDao.queryTradeByTradNum(hql_2, order.getTradeId());
        if(trade.getHasParent().equals("1")) {
            Trade trade2 = tradeDao.queryTradeByTradNum(hql_2, trade.getParentNum());
            trade.setAddressGroup(trade2.getAddressGroup());
            trade.setCustomerTel(trade2.getCustomerTel());
            trade.setExpressType(trade2.getExpressType());
            trade.setExpressFee(trade2.getExpressFee());
            trade.setTradeType(TradeEnum.getDescByCode(trade2.getTradeType()));
        }
        order.setTrade(trade);
        order.setOrderStatus(TradeEnum.getDescByCode(order.getOrderStatus()));
        return order;
    }

    @Override
    public PageBean<Order> queryOrderByPage(Integer currPage, String condition, String state) {
        PageBean<Order> pageBean = new PageBean<>();
        pageBean.setCurrentPage(currPage);
        pageBean.setPageSize(5);
        Map<String, String> params = new HashMap<>();
        String hql_1  = "from Order o where 1=1 ";
        String hql_2 = "select count(1) from Order o where 1=1 ";
        StringBuilder builder = new StringBuilder();
        if(!StringUtils.isEmpty(condition)) {
            builder.append("and o.orderNum like :orderNum ");
            params.put("orderNum", condition);
        }
        if(!StringUtils.isEmpty(state)) {
            builder.append("and o.orderStatus=:orderStatus ");
            params.put("orderStatus", state);
        }
        builder.append("order by o.createTime desc");
        hql_1 += builder.toString();
        hql_2 += builder.toString();
        List<Order> orders = orderDao.queryOrder(hql_1, params, currPage, pageBean.getPageSize());
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        orders.stream().filter(e->{
            e.setOrderStatus(TradeEnum.getDescByCode(e.getOrderStatus()));
           /* String formateTime = simpleDateFormat.format(e.getCreateTime());

            try {
                e.setCreateTime(simpleDateFormat.parse(formateTime));
            } catch (ParseException e1) {
                e1.printStackTrace();
            }*/
            return true;
        }).collect(Collectors.toList());
        Long count = orderDao.countOrder_admin(hql_2, params);
        pageBean.setTotalCount(count==null? 0 : count.intValue());
        pageBean.setResultList(orders);
        return pageBean;
    }

    @Override
    public Order queryOrderDetails_admin(String orderNum)throws Exception {
        String hql_1 = "from Order o where o.orderNum=:orderNum";
        Map<String, String> params = new HashMap<>();
        params.put("orderNum", orderNum);
        Order order = orderDao.queryOrderByOrderNum_admin(hql_1, params);
        String hql_2 = "from Trade t where t.tradeNum=:tradeNum";
        Trade trade = tradeDao.queryTradeByTradNum(hql_2, order.getTradeId());
        if(trade.getHasParent().equals("1")) {
            Trade trade2 = tradeDao.queryTradeByTradNum(hql_2, trade.getParentNum());
            trade.setAddressGroup(trade2.getAddressGroup());
            trade.setCustomerTel(trade2.getCustomerTel());
            trade.setExpressType(trade2.getExpressType());
            trade.setExpressFee(trade2.getExpressFee());
            trade.setTradeType(TradeEnum.getDescByCode(trade2.getTradeType()));
        }
        order.setTrade(trade);
        order.setOrderStatus(TradeEnum.getDescByCode(order.getOrderStatus()));
        return order;
    }

    @Override
    public void updateOrderStatusById_admin(String orderNum, String state)throws Exception {
        String hql_1 = "from Order o where orderNum=:orderNum";
        Map<String, String> params = new HashMap<>();
        params.put("orderNum", orderNum);
        Order order = orderDao.queryOrderByOrderNum_admin(hql_1, params);
        if(order!=null) {
            String statusCode = TradeEnum.getCodeByDesc(state);
            if(statusCode == null) {
                throw new Exception("非法的订单状态");
            }
            if(order.getOrderStatus().equals(TradeEnum.UNSHIPPED.getCode()) && TradeEnum.WAITGET.getCode().equals(statusCode)) {
                order.setOrderStatus(statusCode);
                orderDao.update(order);
            }
        }
    }
}
