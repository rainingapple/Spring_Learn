package cn.rainingapple.spring_api;

import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

public class AfterLog implements AfterReturningAdvice{
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println(
                "执行了" +target.getClass().getName()
        +"的"+method.getName()
                +"方法，结果为"+returnValue);
    }
}
