# Spring-08-AOP

**Aspect-oriented Programming (AOP)** complements Object-oriented Programming (OOP) by providing another way of thinking about program structure. The key unit of modularity in OOP is the class, whereas in AOP **the unit of modularity is the aspect**. Aspects enable the modularization of **concerns** (such as transaction management) that cut across multiple types and objects. (Such concerns are often termed “**crosscutting**” concerns in AOP literature.)

One of the key components of Spring is the AOP framework. While the Spring IoC container does not depend on AOP (meaning you do not need to use AOP if you don’t want to), AOP complements Spring IoC to provide a very **capable middleware** solution.

<!--more-->

**参考**

Spring Core官方文档

https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/core.html#aop

狂神说博客

https://www.cnblogs.com/renxuw/p/12994080.html

https://mp.weixin.qq.com/s/zofgBRRrnEf17MiGZN8IJQ

**Github仓库**

https://github.com/rainingapple/Spring_Learn

## Spring AOP

### AOP is used in the Spring Framework to:

- Provide declarative enterprise services. The most important such service is [declarative transaction management](https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/data-access.html#transaction-declarative).
- Let users implement custom aspects, complementing their use of OOP with AOP.

### AOP Concepts

Let us begin by defining some central AOP concepts and terminology. These terms are not Spring-specific. Unfortunately, AOP terminology i**s not particularly intuitive**. However, it would be even more confusing if Spring used its own terminology.

- **Aspect**: A modularization of a concern that cuts across multiple classes. Transaction management is a good example of a crosscutting concern in enterprise Java applications. In Spring AOP, aspects are implemented by using regular classes (the [schema-based approach](https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/core.html#aop-schema)) or regular classes annotated with the `@Aspect` annotation (the [@AspectJ style](https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/core.html#aop-ataspectj)).
- **Join point**: A point during the execution of a program, such as the execution of a method or the handling of an exception. In Spring AOP, a join point always represents a method execution.
- **Advice**: Action taken by an aspect at a particular join point. Different types of advice include “around”, “before” and “after” advice. (Advice types are discussed later.) Many AOP frameworks, including Spring, model an advice as an interceptor and maintain a chain of interceptors around the join point.
- **Pointcut**: A predicate that matches join points. Advice is associated with a pointcut expression and runs at any join point matched by the pointcut (for example, the execution of a method with a certain name). The concept of join points as matched by pointcut expressions is central to AOP, and Spring uses the AspectJ pointcut expression language by default.
- **Introduction**: Declaring additional methods or fields on behalf of a type. Spring AOP lets you introduce new interfaces (and a corresponding implementation) to any advised object. For example, you could use an introduction to make a bean implement an `IsModified` interface, to simplify caching. (An introduction is known as an inter-type declaration in the AspectJ community.)
- **Target object**: An object being advised by one or more aspects. Also referred to as the “advised object”. Since Spring AOP is implemented by using **runtime proxies**, this object is always a proxied object.
- **AOP proxy**: An object created by the AOP framework in order to implement the aspect contracts (advise method executions and so on). In the Spring Framework, an AOP proxy is a JDK dynamic proxy or a CGLIB proxy.
- **Weaving**: linking aspects with other application types or objects to create an advised object. This can be done at compile time (using the AspectJ compiler, for example), load time, or at runtime. Spring AOP, like other pure Java AOP frameworks, performs weaving at runtime.

### Spring AOP advice types

- **Before advice**: Advice that runs before a join point but that does not have the ability to prevent execution flow proceeding to the join point (unless it throws an exception).
- **After returning advice**: Advice to be run after a join point completes normally (for example, if a method returns without throwing an exception).
- **After throwing advice**: Advice to be run if a method exits by throwing an exception.
- **After (finally) advice**: Advice to be run regardless of the means by which a join point exits (normal or exceptional return).
- **Around advice**: Advice that surrounds a join point such as a method invocation. This is the most powerful kind of advice. Around advice can perform custom behavior before and after the method invocation. It is also responsible for choosing whether to proceed to the join point or to shortcut the advised method execution by returning its own return value or throwing an exception.

## Spring AOP 实例

### 使用Spring API实现AOP

#### Spring execution 表示 Method

execution(modifiers-pattern? ret-type-pattern declaring-type-pattern? name-pattern(param-pattern)throws-pattern?)

- returning type pattern,name pattern, and parameters pattern是必须的.
- ret-type-pattern:\*可以为表示任何返回值,全路径的类名等.
- name-pattern:指定方法名,\*代表所以,set*代表以set开头的所有方法.
- parameters pattern:指定方法参数(声明的类型),(..)代表所有参数,(\*)代表一个参数

#### 真实对象

在已有的数据库增删改查操作基础上，通过Spring AOP加入日志功能。

##### UserService

```java
public interface UserService {
    public void add();
    public void delete();
    public void modify();
    public void select();
}
```

##### UserServiceImpl

```java
public class UserServiceImpl implements UserService{
    @Override
    public void add() {
        System.out.println("加入了一个元素");
    }

    @Override
    public void delete() {
        System.out.println("删除了一个元素");
    }

    @Override
    public void modify() {
        System.out.println("更改了一个元素");
    }

    @Override
    public void select() {
        System.out.println("查询了一个元素");
    }
}
```

#### 日志对象

##### PreLog

```java
public class PreLog implements MethodBeforeAdvice{
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("即将调用"+target.getClass().getName()
                +"的"+method.getName()+"方法");
    }
}
```

##### AfterLog

```java
public class AfterLog implements AfterReturningAdvice{
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println(
                "执行了" +target.getClass().getName()
        +"的"+method.getName()
                +"方法，结果为"+returnValue);
    }
}
```

#### xml配置

##### 引入aop约束

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        https://www.springframework.org/schema/aop/spring-aop.xsd">
</beans>
```

##### beans.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        https://www.springframework.org/schema/aop/spring-aop.xsd">

    <bean id="userserviceimpl" class="cn.rainingapple.UserServiceImpl"></bean>
    <bean id="prelog" class="cn.rainingapple.spring_api.PreLog"></bean>
    <bean id="afterlog" class="cn.rainingapple.spring_api.AfterLog"></bean>

    <aop:config>
        <!--表达式代表返回值任意的、参数任意的、cn.rainingapple.UserServiceImpl中的所有方法-->
        <aop:pointcut id="mypointcut" expression="execution(* cn.rainingapple.UserServiceImpl.*(..) )"/>
        <aop:advisor advice-ref="afterlog" pointcut-ref="mypointcut"></aop:advisor>
        <aop:advisor advice-ref="prelog" pointcut-ref="mypointcut"></aop:advisor>
    </aop:config>

</beans>
```

#### MyTest

```java
public class MyTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        UserService serviceImpl = context.getBean("userserviceimpl", UserService.class);
        serviceImpl.add();
        serviceImpl.delete();
        serviceImpl.modify();
        serviceImpl.select();
    }
}
```

### 使用自定义方式实现AOP

了解即可

#### DiyPointCut

```java
public class DiyPointCut {
    public void before(){
        System.out.println("Do something before invocation");
    }

    public void after(){
        System.out.println("Do something after invocation");
    }
}
```

#### beans_diy.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        https://www.springframework.org/schema/aop/spring-aop.xsd">

    <bean id="userserviceimpl" class="cn.rainingapple.UserServiceImpl"></bean>
    <bean id="diypointcut" class="cn.rainingapple.diypointcut.DiyPointCut"></bean>

    <aop:config>

        <!--指明一个切入点-->
        <aop:pointcut id="mypointcut" expression="execution(* cn.rainingapple.UserServiceImpl.*(..) )"/>

        <!--定义一个切面-->
        <aop:aspect ref="diypointcut">
            <aop:before method="before" pointcut-ref="mypointcut"></aop:before>
            <aop:after method="after" pointcut-ref="mypointcut"></aop:after>
        </aop:aspect>
    </aop:config>

</beans>
```

### 使用注解方式实现AOP

#### AnnotationPointCut

```java
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
```

#### beans_annotation.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        https://www.springframework.org/schema/aop/spring-aop.xsd">

    <bean id="annotationpointcut" class="cn.rainingapple.annotation.AnnotationPointCut"></bean>
    <aop:aspectj-autoproxy></aop:aspectj-autoproxy>

</beans>
```

- 通过aop命名空间的 <aop:aspectj-autoproxy /> 声明自动为spring容器中那些配置@aspectJ切面的bean创建代理，织入切面。
- spring 在内部依旧采用AnnotationAwareAspectJAutoProxyCreator进行自动代理的创建工作，但实现细节隐藏
- <aop:aspectj-autoproxy /> 有一个proxy-target-class属性，默认为false，表示使用jdk动态代理织入增强
- 当proxy-target-class配为true时，表示使用CGLib动态代理技术织入增强。
- 特殊情况下，即使proxy-target-class设置为false，如果目标类没有声明接口，则spring将自动使用CGLib动态代理。

#### 各增强执行顺序

根据实际测试可以得出执行顺序为：

环绕前—before—执行—返回后—after—环绕后