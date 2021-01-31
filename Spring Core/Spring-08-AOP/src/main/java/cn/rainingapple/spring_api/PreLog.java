package cn.rainingapple.spring_api;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class PreLog implements MethodBeforeAdvice{
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("即将调用"+target.getClass().getName()
                +"的"+method.getName()+"方法");
    }
}
