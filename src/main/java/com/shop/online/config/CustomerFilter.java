package com.shop.online.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义过滤器
 * @author YJH
 * @version V1.0 创建时间：2019/3/3 0003
 */
@Component
@Slf4j
public class CustomerFilter extends GenericFilterBean {

    @Value("#{'${exclude.filter.url:/user/login}'.split(';')}")
    List<String> excludeUrls;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String requestUrl = httpServletRequest.getServletPath();
        String param = httpServletRequest.getQueryString();
        log.info("用户请求地址：{}, 请求参数为：{}", requestUrl, param);
        if(!excludeUrls.isEmpty()) {
            for(String excludeUrl : excludeUrls) {
                //非过滤请求url
                if(requestUrl.equals(excludeUrl)) {
                    chain.doFilter(httpServletRequest, httpServletResponse);
                    return;
                }
            }
        }
        HttpSession session = httpServletRequest.getSession();
        if(session.getAttribute("user") == null) {
            session.setAttribute("redirectUrl", requestUrl);
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/user/login?" + param);
        }
        else {
            chain.doFilter(httpServletRequest, httpServletResponse);
        }
    }
}
