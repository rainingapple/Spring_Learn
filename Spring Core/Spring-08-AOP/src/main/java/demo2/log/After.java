package demo2.log;

import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

public class After implements AfterReturningAdvice {
    @Override
    public void afterReturning(Object o, Method method, Object[] objects, Object o1) throws Throwable {
        System.out.println(method.getName()+"执行完毕");
    }
}
