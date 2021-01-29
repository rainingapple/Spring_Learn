# Spring-06-Annotation

基于注释的配置提供了XML设置的替代方法，该配置依赖字节码元数据来连接组件，而不是尖括号声明。通过使用相关类，方法或字段声明上的注释，开发人员无需使用XML来描述bean的连接，而是将配置移入组件类本身。

**那么，可以说注解在配置Spring方面比XML更好吗？**

简短的答案是“取决于情况”。每种方法都有其优缺点，通常，由开发人员决定哪种策略更适合他们。由于定义方式的不同，注释在声明中提供了很多上下文，从而使**配置更短，更简洁**。但是，**XML擅长连接组件而不接触其源代码**。一些开发人员更喜欢将布线放置在靠近源的位置，而另一些开发人员则认为带注释的类不再是POJO，而且，配置变得分散并且难以控制。无论选择如何，Spring都可以容纳两种样式，甚至可以将它们混合在一起。值得指出的是，通过其JavaConfig选项，Spring允许以**非侵入**方式使用注释，而无需接触目标组件的源代码

**参考**

Spring Core官方文档

https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/core.html#beans-annotation-config

狂神说博客

https://www.cnblogs.com/renxuw/p/12994080.html

https://mp.weixin.qq.com/s/dCeQwaQ-A97FiUxs7INlHw

**Github仓库**

https://github.com/rainingapple/Spring_Learn

<!--more-->

## 引入约束

要使用注解开发需要引入对应的约束，即spring-aop

同时引入相应的context

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xmlns:context="http://www.springframework.org/schema/context"
      xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">

</beans>
```

## 使用注解配置Bean

### @Component

#### 使用该注解需要指明需要扫描哪个包

```xml
<!--指定要扫描的包的位置-->
<context:component-scan base-package="cn.rainingapple.pojo"></context:component-scan>
```

#### 使用@Component替代 < bean id="" class=""/>

```java
@Component("user")
public class User {
    private String name = "张三";

    public String getName() {
        return name;
    }
}
```

#### MyTest

```java
public class MyTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        User user = context.getBean("user", User.class);
        System.out.println(user.getName());
    }
}
```

### @Value

#### 使用@Value替代 < property name="" value=""/>

```java
@Component("user")
public class User {
    @Value("张三")
    private String name;

    public String getName() {
        return name;
    }
}
```

### @Component三个衍生注解

为了更好的进行分层，Spring可以使用其它三个注解，功能一样，目前使用哪一个功能都一样。

- @Controller：Controller层
- @Service：service层
- @Repository：dao层

他们和@Component一样都是用作Bean装配，具体等到SpringMVC展开。

### @scope作用域

- **singleton**：单例模式，默认模式。关闭工厂 ，所有的对象都会销毁。
- **prototype**：原型模式。关闭工厂 ，所有的对象不会销毁。内部的垃圾回收机制会回收

```java
@Component("user")
@Scope("prototype")
public class User {
    @Value("张三")
    private String name;

    public String getName() {
        return name;
    }
}
```

## Java Configure 初探

### @Configuration 与 @Bean

使用原生的java configure来代替xml文件，二者起到等效作用

#### pojo类

```java
@Component
public class User_JavaConfig {
    @Value("张三")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User_JavaConfig{" +
                "name='" + name + '\'' +
                '}';
    }
}
```

#### Java Configure类

```java
@Configuration
public class MyConfig {
    @Bean
    // 函数名对应id，函数返回值对应class
    public User_JavaConfig user(){
        return new User_JavaConfig();
    }
}
```

@ComponentScan , @Import() , 也可以同样使用

#### MyTest_JavaConfigure

```java
public class MyTest_JavaConfigure {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        User_JavaConfig user = context.getBean("user", User_JavaConfig.class);
        System.out.println(user.toString());
    }
}
```

具体的Java Based将在Spring MVC 与 Spring Boot中再做详述

