# Spring-07-Proxy

实际生产过程中，在多数情况下我们无法修改源码。

在这种情况下，如何向原来的模块中引入新功能？

一个简单的的想法是写一个代理类，与真实角色共用一个接口，从而方便得添加功能。

后面要学习的 Spring AOP 的底层实现便是代理。

<!--more-->

**参考**

JDK官方文档

https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/reflect/InvocationHandler.html

https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/reflect/Proxy.html

狂神说博客

https://www.cnblogs.com/renxuw/p/12994080.html

https://mp.weixin.qq.com/s/McxiyucxAQYPSOaJSUCCRQ

**Github仓库**

https://github.com/rainingapple/Spring_Learn

## 静态代理

### 引例：购房

我们要写一个系统模拟购房行为，那么大致包含以下角色：

- 购房行为
- 房主
- 中间商
- 买房客户

#### 购房行为（Rent）

```java
public interface Rent {
    void rent();
}
```

#### 房主（Host）

```java
public class Host implements Rent{

    @Override
    public void rent() {
        System.out.println("房主想要卖一个房子");
    }
}
```

#### 中间商（Agency）

```java
public class Agency implements Rent{

    Host host = new Host();

    @Override
    public void rent() {
        host.rent();
        System.out.println("我是中介，代理房主卖房，收取了一定费用");
    }
}
```

#### 买房客户（Customer）

```java
public class Customer {

    Rent rent;

    public void buy(){
        rent = new Agency();
        rent.rent();
        System.out.println("我是客户我要买房，我通过中介买房");
    }
}
```

很明显作为真实角色的房主是无法改变的，但是中间商可以赋予它更多的功能

### 静态代理角色分析

- 抽象角色 : 具体功能的高层抽象，比如上例中的购房这个行为
- 真实角色 : 被代理的角色，比如上例中的房主
- 代理角色 : 代理真实角色，添加一些附属的操作 ，比如上例中的房产中介
- 客户  :  使用代理角色来进行操作的角色，比如上例中的购房客户。

### 静态代理的好处:

- 可以使得我们的真实角色更加纯粹 . 不再去关注一些公共的事情 .
- 公共的业务由代理来完成 . 实现了业务的分工 ,
- 公共业务发生扩展时变得更加集中和方便 .

## 动态代理

设想以下，加入每次写一个代理都要像上面一样静态编写一个类，那最终的项目会变得多么冗杂

我们能不能动态的设置代理呢？

那么什么是动态代理呢？

- 动态代理的角色和静态代理的一样 .

- 动态代理的代理类是动态生成的 . 静态代理的代理类是我们提前写好的

- 动态代理分为两类 : 一类是基于接口动态代理 , 一类是基于类的动态代理

- - 基于接口的动态代理----JDK动态代理
  - 基于类的动态代理--cglib

### 基于JDK的动态代理

**JDK的动态代理需要了解两个类**

核心 :  **InvocationHandler**   和   **Proxy**  

#### InvocationHandler.invoke()

Processes a method invocation on a proxy instance and returns the result. This method will be invoked on an invocation handler when a method is invoked on a proxy instance that it is associated with.

##### Params:

- **proxy** – the proxy instance that the method was invoked on
- **method** – the Method instance corresponding to the interface method invoked on the proxy instance. The declaring class of the Method object will be the interface that the method was declared in, which may be a superinterface of the proxy interface that the proxy class inherits the method through.
- **args** – an array of objects containing the values of the arguments passed in the method invocation on the proxy instance, or null if interface method takes no arguments. Arguments of primitive types are wrapped in instances of the appropriate primitive wrapper class, such as java.lang.Integer or java.lang.Boolean.

##### Returns:

the value to return from the method invocation on the proxy instance. 

#### Proxy.newProxyInstance()

Returns an instance of a proxy class for the specified interfaces that dispatches method invocations to the specified invocation handler.
Proxy.newProxyInstance throws IllegalArgumentException for the same reasons that Proxy.getProxyClass does.

##### Params:

- **loader** – the class loader to define the proxy class  

- **interfaces** – the list of interfaces for the proxy class to implement
- **h** – the invocation handler to dispatch method invocations to

##### Returns:

a proxy instance with the specified invocation handler of a proxy class that is defined by the specified class loader and that implements the specified interfaces

#### MyInvocationHandler

实现InvocationHandler接口，实现invoke方法。

通过Proxy.newProxyInstance获得代理实例

```java
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
```

#### MyTest

```java
public class MyTest_Dynamic {
    public static void main(String[] args) {
        MyInvocationHandler myInvocationHandler = new MyInvocationHandler();
        myInvocationHandler.setRent(new Host());
        Rent proxy = (Rent) myInvocationHandler.getProxy();
        proxy.rent();
    }
}
```

#### 通用的代理模板

```java
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
```

类型是内部确定的，需要时候强转即可

#### MyTest

```java
public class MyTest_GD {
    public static void main(String[] args) {
        Generate_InvocationHandler generate_invocationHandler = new Generate_InvocationHandler();
        generate_invocationHandler.setTarget(new Host());
        Rent proxy = (Rent) generate_invocationHandler.getProxy();
        proxy.rent();
    }
}
```

### 动态代理的好处

- 可以使得我们的真实角色更加纯粹 . 不再去关注一些公共的事情 .
- 公共的业务由代理来完成 . 实现了业务的分工 ,
- 公共业务发生扩展时变得更加集中和方便 .

同时相比于静态代理增加了：

- 动态代理一般代理某一类业务，通过代理接口可以代理多个实体类，简化开发



