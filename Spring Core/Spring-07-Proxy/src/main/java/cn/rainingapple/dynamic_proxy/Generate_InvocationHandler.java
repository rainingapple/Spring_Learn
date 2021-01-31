package cn.rainingapple.dynamic_proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//通用的代理模板
public class Generate_InvocationHandler implements InvocationHandler {

    Object target;

    public void setTarget(Object target) {
        this.target = target;
    }

    public Object getProxy(){
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), target.getClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("在代理前做了一些事");
        Object result = method.invoke(target, args);
        System.out.println("在代理后做了一些事");
        return result;
    }
}
