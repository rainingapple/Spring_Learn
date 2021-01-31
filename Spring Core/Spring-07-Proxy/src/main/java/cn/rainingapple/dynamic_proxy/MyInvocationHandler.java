package cn.rainingapple.dynamic_proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyInvocationHandler implements InvocationHandler {

    Rent rent;

    public void setRent(Rent rent){
        this.rent=rent;
    }

    public Object getProxy(){
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), rent.getClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("我在之前做了一些事");
        Object result = method.invoke(rent,args);
        System.out.println("我在之后也做了一些事");
        return result;
    }
}
