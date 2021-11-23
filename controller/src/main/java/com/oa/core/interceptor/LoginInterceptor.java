package com.oa.core.interceptor;


import com.oa.core.bean.Loginer;
import org.springframework.util.Base64Utils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 验证是否登录，如果需要登录，则跳转到登录页面
 *
 * @author zxd
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {

            Logined authPassport = ((HandlerMethod) handler).getMethodAnnotation(Logined.class);

            if (authPassport == null) {
                return true;
            } else {
                Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
                if (loginer != null) {
                    return true;
                } else {
                    String referer = request.getRequestURL().toString();
                    String queryString = request.getQueryString();
                    if (queryString != null) {
                        referer += "?" + queryString;
                    }
                    String url = Base64Utils.encodeToString(referer.getBytes());
                    response.sendRedirect(request.getContextPath() + "/login.do");
                    return false;
                }
            }
        } else {
            return true;
        }
    }
}