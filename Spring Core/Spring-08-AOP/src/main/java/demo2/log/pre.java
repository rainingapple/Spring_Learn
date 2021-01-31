package demo2.log;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class pre implements MethodBeforeAdvice {
    public void before(Method method, Object[] objects, Object o) throws Throwable {
        System.out.println(method.getName()+"将要执行");
    }
}
