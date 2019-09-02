package com.shop.online.web.rest;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.shop.online.common.DelayDataBean;
import com.shop.online.constant.AlipayConfig;
import com.shop.online.model.Address;
import com.shop.online.model.Order;
import com.shop.online.model.User;
import com.shop.online.service.AddressService;
import com.shop.online.service.RabbitMqService;
import com.shop.online.service.OrderService;
import com.shop.online.service.ShopCartService;
import com.shop.online.util.GenOrderNo;
import com.shop.online.vo.CartItemVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/5 0005
 */
@Controller
@RequestMapping("/orders")
@Slf4j
public class OrderController {


    @Resource
    private ShopCartService shopCartService;

    @Resource
    private AddressService addressService;

    @Resource
    private OrderService orderService;

    /**
     * 确认订单页面
     */
    @PostMapping("/orderConfirm")
    public String orderConfirmHtml(@RequestParam(value = "skuIds[]") String[] skuIds, @RequestParam(value = "cartItems[]") String[] cartItems, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        String userId = user.getId();
        String itemToken = Arrays.stream(cartItems).collect(Collectors.joining("_"));
        List<String> tokenList = (List<String>) session.getAttribute("item_token_list");
        boolean repeated = false;
        //已下单，重复提交
        if (tokenList != null && tokenList.contains(itemToken)) {
            repeated = true;
        }
        if (!repeated) {
            try {
                List<CartItemVo> cartItemVoList = shopCartService.queryItemsBySkuId(userId, skuIds);
                int totalCount = cartItemVoList.stream().mapToInt(e -> Integer.parseInt(e.getSkuItemCount())).sum();
                double totalPrice = cartItemVoList.stream().mapToDouble(e -> {
                    BigDecimal price = new BigDecimal(e.getPrice());
                    BigDecimal result = price.multiply(new BigDecimal(e.getSkuItemCount())).setScale(2, BigDecimal.ROUND_HALF_UP);
                    return Double.parseDouble(result.toPlainString());
                }).sum();
                //获取该用户的收货地址
                List<Address> addressList = addressService.queryAllAddressByUser(userId);
                model.addAttribute("cartItems", cartItemVoList);
                model.addAttribute("addressList", addressList);
                model.addAttribute("totalPrice", totalPrice);
                model.addAttribute("totalCount", totalCount);
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
        }
        model.addAttribute("repeated", repeated);
        return "back/user/orderConfirmation";
    }

    /**
     * 创建订单
     */
    @PostMapping("/createOrder")
    public String createOrder(@RequestParam(value = "skuIds[]") String[] skuIds, @RequestParam(value = "cartItems[]") String[] cartItems, String addressId, String totalAmounts, HttpSession session, RedirectAttributes redirectAttributes) {

        String itemToken = Arrays.stream(cartItems).collect(Collectors.joining("_"));
        User user = (User) session.getAttribute("user");
        String userId = user.getId();
        boolean repeated = false;
        List<String> tokenList = (List<String>) session.getAttribute("item_token_list");
        if (tokenList == null || tokenList.indexOf(itemToken) == -1) {
            tokenList = new ArrayList<>();
            session.setAttribute("item_token_list", tokenList);
            tokenList.add(itemToken);
        } else {
            //重复提交订单
            if (tokenList.contains(itemToken)) {
                repeated = true;
            }
        }
        try {
            //重复提交订单
            //count = orderService.countTradeByItemToken(itemToken, userId);
            orderService.addTradeLock(itemToken);
        } catch (Exception e) {
            repeated = true;
        }
        if (repeated) {
            return "error";
        }
        try {
            //生成订单
            Map<String, String> data = orderService.addOrder(skuIds, addressId, user);
            redirectAttributes.addFlashAttribute("payData", data);
            //Map<String, String> queueData = new HashMap<>();
            DelayDataBean delayDataBean = new DelayDataBean();
            delayDataBean.setSkuIds(skuIds);
            delayDataBean.setTradeNo(data.get("tradeId"));
            delayDataBean.setItemToken(itemToken);
            rabbitMqService.sendDelayData(delayDataBean);
            int index = tokenList.indexOf(itemToken);
            tokenList.remove(index);
            //orderService.deleteTradeLock(itemToken);
            return "redirect:/orders/payOrder";
        } catch (Exception e) {
            int index = tokenList.indexOf(itemToken);
            tokenList.remove(index);
            orderService.deleteTradeLock(itemToken);
            e.printStackTrace();
            return "error";
        }

    }

    /**
     * 请求支付宝接口进行支付
     *
     * @param payData
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/payOrder", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String payOrder(@ModelAttribute(value = "payData") Map<String, String> payData, HttpSession session, Model model) {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        //alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = payData.get("tradeId");
        //付款金额，必填
        String total_amount = payData.get("totalAmount");
        //订单名称，必填
        String subject = payData.get("orderName");
        //商品描述，可空
        String body = payData.get("description");
        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "\"timeout_express\":\"" + AlipayConfig.pay_timeout + "\"}");
        //请求
        try {
            String result = alipayClient.pageExecute(alipayRequest).getBody();
            //model.addAttribute("result", result);
            //return "back/user/orderPay";
            return result;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 从“我的订单页面”的支付请求,单独一个订单生成一笔交易
     */
    @GetMapping("/toOrderPay")
    public String toOrderPay(String orderId, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        String userId = user.getId();
        //查询订单
        Map<String, String> data;
        try {
            data = orderService.queryOrderForPay(orderId, userId);
            redirectAttributes.addFlashAttribute("payData", data);
            return "redirect:/orders/payOrder";
        } catch (Exception e) {
            e.printStackTrace();
            log.info("订单{}交易失败：{}", orderId, e.getMessage());
            return "error";
        }
    }

    /**
     * 支付回调接口
     *
     * @param request
     * @param session
     */
    @RequestMapping(value = "/notify_url")
    @ResponseBody
    public String callback_alipay(HttpServletRequest request, HttpSession session) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        //调用SDK验证签名
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failure";
        }
        //验证成功
        if (signVerified) {

            //交易状态
            String tradeStatus = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
            //交易成功或完成
            if (tradeStatus.equals("TRADE_SUCCESS") || tradeStatus.equals("TRADE_FINISHED")) {
                try {
                    return orderService.aliPayCallBack(requestParams);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return "failure";
        } else {//验证失败
            //调试用，写文本函数记录程序运行情况是否正常
            String sWord = AlipaySignature.getSignCheckContentV1(params);
            log.info("支付宝验证失败,参数为：{}----------------------", sWord);
            return "failure";
        }
    }

    @RequestMapping(value = "/payOrderTest", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String payOrderTest() {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        //alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = GenOrderNo.getOrderCode(23923L);
        //付款金额，必填
        String total_amount = "10.00";
        //订单名称，必填
        String subject = new String("测试水电费");
        //商品描述，可空
        String body = new String("测试");
        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        /*try {
            String result = alipayClient.pageExecute(alipayRequest).getBody();
            return result;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return null;
        }*/
        return null;
    }

    @Resource
    private RabbitMqService rabbitMqService;

    /**
     * 更新订单状态
     *
     * @param orderId
     * @param state
     * @param session
     */
    @PostMapping("/updatestatus")
    @ResponseBody
    public String updateOrderStatus(String orderId, String state, String tradeId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        String userId = user.getId();
        if (!StringUtils.isEmpty(orderId) && !StringUtils.isEmpty(state)) {
            int change = 0;
            try {
                change = orderService.updateOrderStatusById(orderId, userId, state, tradeId);
                if (change == 1) {
                    log.info("更新状态成功");
                }
                return "success";
            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }
        log.info("订单更新失败:参数错误");
        return "error";
    }

    @GetMapping("/addComment")
    public String CommentHtml(String orderId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        String userId = user.getId();
        try {
            Order order = orderService.queryOrderByOrderNum(orderId, userId);
            model.addAttribute("order", order);
            return "/back/comment/addComment";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/istakegoods")
    @ResponseBody
    public String istakeGoods(String orderId, HttpSession session) {
        User user = (User)session.getAttribute("user");
        String userId = user.getId();
        return orderService.isTakeGoodes(orderId, userId);
    }

}
