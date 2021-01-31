package cn.rainingapple.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
public class AnnotationPointCut {
    @Before("execution(* cn.rainingapple.UserServiceImpl.*(..))")
    public void before(){
        System.out.println("Do something before invocation");
    }

    @After("execution(* cn.rainingapple.UserServiceImpl.*(..))")
    public void after(){
        System.out.println("Do something after invocation");
    }

    @Around("execution(* cn.rainingapple.UserServiceImpl.*(..))")
    public void around(ProceedingJoinPoint jp) throws Throwable {
        System.out.println("环绕前");
        System.out.println("签名:"+jp.getSignature());
        //执行目标方法proceed
        Object proceed = jp.proceed();
        System.out.println("环绕后");
        System.out.println(proceed);
    }

    @AfterReturning("execution(* cn.rainingapple.UserServiceImpl.*(..))")
    public void sss(){
        System.out.println("返回了");
    }
}
