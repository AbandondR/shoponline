package com.shop.online.config;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/7 0007
 */
public class DelegatingFilterProxy implements Filter {

    private String targetBean;
    private Filter proxy;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.targetBean = filterConfig.getInitParameter("targetBean");
        ServletContext servletContext = filterConfig.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        this.proxy = (Filter) webApplicationContext.getBean(targetBean);
        this.proxy.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        proxy.doFilter(request, response, chain);
    }

    @Override
    public void destroy() {

    }
}
