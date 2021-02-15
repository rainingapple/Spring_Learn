package cn.rainingapple.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterCeptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println(request.getRequestURI());
        if (request.getRequestURI().equals("/SpringMVC_09_Blocker_war_exploded/login")){
            System.out.println(1);
            if (request.getParameter("username").equals("admin")){
                System.out.println(2);
                if(request.getParameter("pwd").equals("123456")){
                    System.out.println(3);
                    return true;
                }
            }
        }
        return false;
    }
}
