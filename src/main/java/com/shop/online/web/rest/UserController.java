package com.shop.online.web.rest;

import com.alibaba.fastjson.JSONObject;
import com.shop.online.common.PageBean;
import com.shop.online.common.SendRedirectObj;
import com.shop.online.constant.HttpRequestType;
import com.shop.online.constant.TradeEnum;
import com.shop.online.model.Address;
import com.shop.online.model.Order;
import com.shop.online.model.User;
import com.shop.online.service.AddressService;
import com.shop.online.service.OrderService;
import com.shop.online.service.ShopCartService;
import com.shop.online.service.UserService;
import com.shop.online.util.MailUitls;
import com.shop.online.util.SendSmsUtil;
import com.shop.online.vo.CartItemVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/1/8 0008
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    private static final long OVER_TIME = 3 * 60 * 1000;
    @Resource
    private UserService userService;

    @GetMapping("/login")
    public String loginHtml() {
        return "user/login-user";
    }

    @PostMapping("/loginValidation")
    public ModelAndView login(@RequestParam final String password, @RequestParam(name = "username") final String userName,
                              HttpSession session, HttpServletRequest request) {
        User user = new User();
        String regex = "^1\\d{10}$";
        Pattern pattern = Pattern.compile(regex);
        if (!StringUtils.isEmpty(userName) && pattern.matcher(userName).matches()) {
            user.setTelePhone(userName);
        } else {
            user.setNickName(userName);
        }
        user.setPassword(password);
        String passCodeServer = (String) session.getAttribute("passcode");
        String passCodeClient = request.getParameter("passcode");
        if (!passCodeClient.equalsIgnoreCase(passCodeServer)) {
            session.setAttribute("loginMsg", "验证码错误，请重新输入");
            return new ModelAndView(new RedirectView("/user/login"));
        }
        try {
            User userInfo = userService.findUser(user);
            session.setAttribute("user", userInfo);
            session.setAttribute("loginMsg", null);
            log.info("{}登录成功，登录时间：{}", userName, new SimpleDateFormat("YYYY-mm-dd HH:mm:ss").format(new Date()));
            SendRedirectObj sendRedirectObj = (SendRedirectObj) session.getAttribute("redirectObj");
            if (sendRedirectObj != null) {
                if (HttpRequestType.ASYNC_REQUEST.getCode().equals(sendRedirectObj.getFlag())) {
                    return new ModelAndView(new RedirectView(sendRedirectObj.getRequestUri(), false, true, false));
                }
                if (sendRedirectObj.getMethodType().equalsIgnoreCase("get")) {
                    return new ModelAndView(new RedirectView(sendRedirectObj.getRequestUri(), false, true), sendRedirectObj.getParams());
                }
                if (sendRedirectObj.getMethodType().equalsIgnoreCase("post")) {
                    return new ModelAndView(new RedirectView(sendRedirectObj.getRequestUri(), false, true, false), sendRedirectObj.getParams());
                }
                session.setAttribute("redirectObj", null);
            } else {
                return new ModelAndView(new RedirectView("/portal/index"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("登录用户：{}验证失败：{}", userName, e.getMessage());
            session.setAttribute("loginMsg", e.getMessage());
        }
        return new ModelAndView(new RedirectView("/user/login"));
    }

    @PostMapping("/sendSms")
    @ResponseBody
    public String sendSms(HttpSession session, @RequestParam("number") String number) {
        try {

            //注册手机号码查重
            if (!StringUtils.isEmpty(number)) {
                int count = userService.checkTeleUnique(number);
                if (count > 0) {
                    return "checkedFailed";
                }
            }
            JSONObject json = null;
            //生成6位验证码
            String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
            //发送短信
            boolean result = SendSmsUtil.sendSms(verifyCode, number);
            if (result) {
                json = new JSONObject();
                json.put("verifyCode", verifyCode);
                json.put("createTime", System.currentTimeMillis());
                json.put("mobile", number);
                // 将认证码存入SESSION
                session.setAttribute("verifyCode", json);
                return "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fail";
    }

    /**
     * 请求注册页面
     *
     * @return
     */
    @GetMapping("/register")
    public String register() {
        return "user/register";
    }

    @Resource
    private ShopCartService shopCartService;
    /**
     * 注册成功同时登陆成功，跳转到首页；注册失败，回显错误原因，重新注册
     * 注册处理接口
     *
     * @return
     */
    @PostMapping("/register/handler")
    public String registerHandler(String mobile, String loginName, String password, String passcode, HttpSession session) {
        JSONObject json = (JSONObject) session.getAttribute("verifyCode");
        String verifyCode = json.getString("verifyCode");
        long startTime = json.getLong("createTime");
        String msg;
        if (!passcode.equals(verifyCode)) {
            msg = "验证码错误";
            session.setAttribute("registerMsg", msg);
            return "redirect:/user/register";
        }
        if (!mobile.equals(json.getString("mobile"))) {
            msg = "手机号码错误";
            session.setAttribute("registerMsg", msg);
            return "redirect:/user/register";
        }
        if (System.currentTimeMillis() - startTime >= OVER_TIME) {
            msg = "验证码已过期,请重新获取";
            session.setAttribute("registerMsg", msg);
            return "redirect:/user/register";
        }
        int count = userService.findUserByName(loginName);
        if(count > 0) {
            msg = "该用户名已存在";
            session.setAttribute("registerMsg", msg);
            return "redirect:/user/register";
        }
        User user = new User();
        user.setPassword(password);
        user.setNickName(loginName);
        user.setTelePhone(mobile);
        user.setStatus("1");
        try {
            String id = userService.register(user);
            user.setId(id);
            session.setAttribute("user", user);
            if(session.getAttribute("registerMsg")!=null) {
                session.removeAttribute("registerMsg");
            }
            shopCartService.generateCart(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/portal/index";
    }

    /**
     * 忘记密码
     * @return
     */
    @GetMapping("/forgotpassw")
    public String forgetPasswHtml() {
        return "user/forgetpassw";
    }
    @PostMapping("/sendSmsForForgot")
    public String sendSmsForForgot(HttpSession session, String number) {
        JSONObject json = null;
        //生成6位验证码
        String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
        //发送短信
        boolean result = false;
        try {
            result = SendSmsUtil.sendSms(verifyCode, number);
            if (result) {
                json = new JSONObject();
                json.put("verifyCode", verifyCode);
                json.put("createTime", System.currentTimeMillis());
                json.put("mobile", number);
                // 将认证码存入SESSION
                session.setAttribute("verifyCodeForForgot", json);
                return "success";
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return "fail";
    }

    /**
     * 找回密码验证
     * @param session
     * @param mobile
     * @param passcode
     * @return
     */
    @PostMapping("/forgotValidation")
    public String forgotPasswHandler(HttpSession session, String mobile, String passcode) {
        JSONObject json = (JSONObject) session.getAttribute("verifyCodeForForgot");
        String verifyCode = json.getString("verifyCode");
        long startTime = json.getLong("createTime");
        String msg;
        if (!passcode.equals(verifyCode)) {
            msg = "验证码错误";
            session.setAttribute("ForgotPasswordMsg", msg);
            return "redirect:/user/forgotpassw";
        }
        if (!mobile.equals(json.getString("mobile"))) {
            msg = "手机号码错误";
            session.setAttribute("ForgotPasswordMsg", msg);
            return "redirect:/user/forgotpassw";
        }
        if (System.currentTimeMillis() - startTime >= OVER_TIME) {
            msg = "验证码已过期,请重新获取";
            session.setAttribute("ForgotPasswordMsg", msg);
            return "redirect:/user/forgotpassw";
        }
        session.setAttribute("forgot-tele", mobile);
        return "user/resetpassw";
    }

    @PostMapping("/resetpassw")
    public String resetPassw(HttpSession session, String password) {
        String mobile = (String)session.getAttribute("forgot-tele");
        if(StringUtils.isEmpty(mobile)) {
            return "redirect:/user/forgotpassw";
        }
        int change = userService.updatePassword(mobile, password);
        return null;
    }

    @GetMapping("infoManage")
    public String userInfoHtml(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        //未登录/登陆过期
        if (user == null) {
            return "redirect:login";
        }
        log.info("用户信息{}", user);
        try {
            User userInfo = userService.findUser(user);
            model.addAttribute(userInfo);
            return "back/user/user-info";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 修改用户信息
     *
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public String updateUser(User user, HttpSession session) {
        String userId = ((User)session.getAttribute("user")).getId();
        User origin = userService.findUserById(userId);
        if (origin == null) {
            log.info("当前用户{},session过期",user.getNickName());
            return "expire";
        }
        origin.setBirthday(user.getBirthday());
        origin.setRealName(user.getRealName());
        origin.setGender(user.getGender());
        origin.setLastModify(new Date());
        try {
            userService.updateUser(origin);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";

    }

    /**
     * 密码修改
     *
     * @return
     */
    @GetMapping("/modifyPass")
    public String modifyPassHtml() {
        return "back/user/editPass";
    }


    /**
     * 初始密码校验
     *
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/passcValidation")
    @ResponseBody
    public String passCodeValidation(String username, String password) {
        User user = new User();
        user.setPassword(password);
        user.setNickName(username);
        try {
            userService.findUser(user);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        }
    }

    /**
     * 修改密码--短信验证码
     *
     * @param session
     * @return
     */
    @PostMapping("/sendSFMP")
    @ResponseBody
    public String sendSmsForModifyPassc(HttpSession session) {
        String userId = ((User)session.getAttribute("user")).getId();
        User user = userService.findUserById(userId);
        if (user != null) {
            if (!StringUtils.isEmpty(user.getTelePhone())) {
                JSONObject json = null;
                String number = user.getTelePhone();
                //生成6位验证码
                //String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
                String verifyCode = "123456";
                //发送短信
                try {
                    //boolean result = SendSmsUtil.sendSms(verifyCode, number);
                    boolean result = true;
                    if (result) {
                        json = new JSONObject();
                        json.put("verifyCode", verifyCode);
                        json.put("createTime", System.currentTimeMillis());
                        json.put("mobile", number);
                        // 将认证码存入SESSION
                        session.setAttribute("verifyCodeForPass", json);
                        return "success";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "fail";
            }
        }
        //登陆过期
        return "login_expire";

    }

    /**
     * 短信验证码验证
     *
     * @param session
     * @param code
     * @return
     */
    @RequestMapping("/validateCode")
    @ResponseBody
    public String validateCode(HttpSession session, String code) {
        String userId = ((User)session.getAttribute("user")).getId();
        User user = userService.findUserById(userId);
        String mobile = user.getTelePhone();
        JSONObject json = (JSONObject) session.getAttribute("verifyCodeForPass");
        String verifyCode = json.getString("verifyCode");
        long startTime = json.getLong("createTime");
        String msg;
        if (System.currentTimeMillis() - startTime >= OVER_TIME) {
            return "timeout";
        }
        if (!code.equals(verifyCode) || !mobile.equals(json.getString("mobile"))) {
            msg = "验证码错误";
            return "failure";
        }
        return "success";
    }

    /**
     * 密码修改处理
     *
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/repassHandler")
    @ResponseBody
    public String rePasswValidation(User user, HttpSession session) {
        try {

            String msg = userService.rePass(user);
            if ("success".equals(msg)) {
                ((User) session.getAttribute("user")).setPassword(user.getPassword());
                session.removeAttribute("user");
            }
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        }
    }

    @Resource
    private AddressService addressService;

    @GetMapping("/addressManage")
    public String addressHtml(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        try {
            List<Address> addressList = addressService.queryAllAddressByUser(user.getId());
            model.addAttribute("addressList", addressList);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "back/user/myAddress";
    }

    /**
     * 跳转到购物车界面
     *
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/cart")
    public String CartHtml(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        String userId = user.getId();
        try {
            List<CartItemVo> cartItemList = userService.queryCartItemList(userId);
            model.addAttribute("cartItemList", cartItemList);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "back/user/cart";
    }

    @Resource
    private OrderService orderService;

    /**
     * 个人订单页面
     */
    @GetMapping("/orders/{currPage}")
    public String OrderManageHtml(@PathVariable(name = "currPage") int currPage, String state, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        String userId = user.getId();
        PageBean<Order> pageBean = null;

        try {
            //state = URLEncoder.encode(state, "utf-8");
            if (state != null) {
                state = new String(state.getBytes("ISO8859-1"), "utf-8");
            }
            String stateCode = TradeEnum.getCodeByDesc(state);
            pageBean = orderService.queryOrderByUser(currPage, stateCode, userId);
            log.info("分页查找订单类型为{}的订单", state);
            //统计各个状态的订单数量
            Map<String, Long> map = orderService.countStateByUser(userId);

            model.addAttribute("pageBean", pageBean);
            model.addAttribute("orderStates", map);
            model.addAttribute("state", state);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "back/user/order";
    }

    /**
     * 订单详情
     * @param orderNum
     * @param session
     */
    @GetMapping("/order/detail/{id}")
    public String orderDetail(@PathVariable(name="id") String orderNum, HttpSession session, Model model) {
        User user = (User)session.getAttribute("user");
        try {
            Order order = orderService.queryOrderDetails(orderNum, user.getId());
            model.addAttribute("order", order);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        //String hql = "select * from Order o left join Trade t on o.tradeId=t.tradeNum where o.orderNum=:orderNum"
        return "back/user/orderDetail";
    }

    @GetMapping("/security")
    public String securityHtml(HttpSession session, Model model) {
        String userId = ((User)session.getAttribute("user")).getId();
        User user = userService.findUserById(userId);
        model.addAttribute("userInfo", user);
        return "back/user/security-edit";
    }

    @GetMapping("/security/tele")
    public String securityTeleHtml(HttpSession session) {
        String userId = ((User)session.getAttribute("user")).getId();
        User user = userService.findUserById(userId);
        String tele = user.getTelePhone();
        String verifyCode = "123456";
        try {
            //boolean result = SendSmsUtil.sendSms(verifyCode, tele);
            JSONObject json = null;
            if (true) {
                json = new JSONObject();
                json.put("verifyCode", verifyCode);
                json.put("createTime", System.currentTimeMillis());
                json.put("mobile", tele);
                // 将认证码存入SESSION
                session.setAttribute("verifyCodeForUpdateTele", json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "back/user/security-tele";
    }

    @PostMapping("/telValidation")
    @ResponseBody
    public String telValidation(String code, HttpSession session) {
        JSONObject json = (JSONObject) session.getAttribute("verifyCodeForUpdateTele");
        String verifyCode = json.getString("verifyCode");
        long startTime = json.getLong("createTime");
        String msg;
        if (System.currentTimeMillis() - startTime >= OVER_TIME) {
            return "timeout";
        }
        if (!code.equals(verifyCode)) {
            msg = "验证码错误";
            return "failure";
        }
        return "success";

    }

    @PostMapping("/sendSmsForUpdateTel")
    @ResponseBody
    public String sendSmsForUpdateTel(String telephone, HttpSession session) {
        return sendSms(session, telephone);
    }

    @PostMapping("/updateTel")
    @ResponseBody
    public String updateTel(String code, String telephone, HttpSession session) {
        JSONObject json = (JSONObject) session.getAttribute("verifyCode");
        String verifyCode = json.getString("verifyCode");
        long startTime = json.getLong("createTime");
        String msg;
        if (!code.equals(verifyCode)) {
            msg = "验证码错误";
            return "failure";
        }
        if (!telephone.equals(json.getString("mobile"))) {
            msg = "手机号码错误";
            return "teleError";
        }
        if (System.currentTimeMillis() - startTime >= OVER_TIME) {
            msg = "验证码已过期,请重新获取";
            return "timeout";
        }
        User user = (User) session.getAttribute("user");
        user.setTelePhone(telephone);
        try {
            userService.updateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    @GetMapping("/security/email")
    public String updateEmailHtml(HttpSession session, Model model) {
        String userId = ((User)session.getAttribute("user")).getId();
        User user = userService.findUserById(userId);
        model.addAttribute("userInfo", user);
        return "back/user/security-email";
    }

    @PostMapping("/security/updateEmail")
    @ResponseBody
    public String updateEmail(String email, HttpSession session) {
        String userId = ((User)session.getAttribute("user")).getId();
        User user = userService.findUserById(userId);
        user.setEMail(email);
        String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
        user.setActiveCode(verifyCode);
        user.setIsActiveEmail("0");
        MailUitls.sendMail(email, verifyCode, user.getId());
        try {
            userService.updateUser(user);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        }
    }

    @GetMapping("/active")
    @ResponseBody
    public String activeEmail(String code, String id) {
        User user = userService.findUserById(id);
        if(user.getActiveCode().equals(code)) {
            user.setIsActiveEmail("1");
            try {
                userService.updateUser(user);
                return "激活成功";
          } catch (Exception e) {
                e.printStackTrace();
                return "激活失败";
            }
        }
        return "激活码错误";
    }

    @GetMapping("/security/email/reactive")
    @ResponseBody
    public String activeRequest(String email, HttpSession session){
        return updateEmail(email, session);
    }

    @GetMapping("/logout")
    public String logOut(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/portal/index";
    }
}

