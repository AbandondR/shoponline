package com.shop.online.config;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 管理员权限拦截器
 */
public class AdminRootInterceptor implements HandlerInterceptor {

    //拦截之前执行的方法
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        System.out.println("=======拦截到方法=======");
        String uri = httpServletRequest.getRequestURI();
        System.out.println("=======请求路径为:"+uri);
        String path = httpServletRequest.getContextPath();
        System.out.println("=======项目根路径为:"+path);
        //如果是跳转到登录页面或者验证登录，就放行
        if(uri.equals("/admin/login") || uri.equals("/admin/loginHandler")){
            return true;
        }
        //如果没有用户登录，就跳转到登录界面
        if(httpServletRequest.getSession().getAttribute("admin") == null){
            System.out.println("=======用户登录=======");
            httpServletResponse.sendRedirect(path + "/admin/login");
            return false;
        }
        /*//如果当前登录的用户权限不够，就跳转到404页面
        Integer root = (Integer) httpServletRequest.getSession().getAttribute("root");
        if(root == 0){
            System.out.println("=======用户权限不够=======");
            httpServletResponse.sendRedirect(path + "/404.jsp");
            return false;
        }*/
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
