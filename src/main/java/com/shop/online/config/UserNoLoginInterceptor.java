package com.shop.online.config;

import com.shop.online.common.SendRedirectObj;
import com.shop.online.constant.HttpRequestType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.VM;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.Map;

/**
 * 用户未登录拦截器，防止未登录用户访问用户资料页面
 */
@Slf4j
public class UserNoLoginInterceptor implements HandlerInterceptor {


    private static final String POST_METHOD = "POST";
    private static final String GET_METHOD = "GET";
    private static final String MULTIPART = "multipart/";
    private static final String LOGIN_URI = "/user/login";
    public String[] allowUrls;//还没发现可以直接配置不拦截的资源，所以在代码里面来排除

    public void setAllowUrls(String[] allowUrls) {
        this.allowUrls = allowUrls;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        log.info("======拦截到请求======");
        String path = request.getContextPath();
        log.info("ContextPath:{}", path);
        //得到请求的URL地址
        String requestUrl = request.getRequestURL().toString();
        log.info("requestURL:{}", requestUrl);
        HttpSession session = request.getSession();
        if(session.getAttribute("user") != null) {
            return true;
        }
        SendRedirectObj redirectObj = new SendRedirectObj();
        String method = request.getMethod();
        String contentType = request.getContentType();
        String flag = "XmlHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With")) ? HttpRequestType.ASYNC_REQUEST.getCode() : HttpRequestType.SYNC_REQUEST.getCode();
        redirectObj.setMethodType(method);
        redirectObj.setFlag(flag);
        //传统（同步）请求
        if(flag.equals(HttpRequestType.SYNC_REQUEST.getCode())) {
            if(POST_METHOD.equalsIgnoreCase(method) && contentType.toLowerCase(Locale.ENGLISH).startsWith(MULTIPART)) {
                //TODO 解析参数

            }
            else {
                Map<String, String[]> params = request.getParameterMap();
                ServletInputStream stream = request.getInputStream();
                byte[] bytes = new byte[stream.available()];
                stream.read(bytes);
                log.info("content:{}", new String(bytes));
                redirectObj.setParams(params);
                redirectObj.setRequestUri(request.getRequestURI());
                session.setAttribute("redirectObj", redirectObj);
                response.sendRedirect(LOGIN_URI);
            }
        }
        //异步请求
        else if(flag.equals(HttpRequestType.ASYNC_REQUEST.getCode())) {
            String referer = request.getHeader("Referer");
            redirectObj.setRequestUri(referer);
            session.setAttribute("redirectObj", redirectObj);
            response.getWriter().append("redirect");
        }


        //得到请求的资源
        /*String requestUri = request.getRequestURI();
        log.info("requestUri:{}", requestUri);*/
        //得到请求的URL地址中附带的参数
        String queryString = request.getQueryString();
        log.info("queryString:{}", queryString);
        //得到来访者的IP地址
        String remoteAddr = request.getRemoteAddr();
        log.info("remoteAddr:{}", remoteAddr);
        String remoteHost = request.getRemoteHost();
        log.info("remoteHost:{}", remoteHost);
        int remotePort = request.getRemotePort();
        log.info("remotePort:{}", remotePort);
        String remoteUser = request.getRemoteUser();
        log.info("remoteUser:{}", remoteUser);
        //得到请求URL地址时使用的方法
        //String method = request.getMethod();
        log.info("method:{}", method);
        String pathInfo = request.getPathInfo();
        log.info("pathInfo:{}", pathInfo);
        //获取WEB服务器的IP地址
        String localAddr = request.getLocalAddr();
        log.info("localAddr:{}", localAddr);
        //获取WEB服务器的主机名
        String localName = request.getLocalName();
        log.info("localName:{}", localName);

        log.info("=====请求参数=====");
        log.info("======requestInfo---END======");
        return false;
    }
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
