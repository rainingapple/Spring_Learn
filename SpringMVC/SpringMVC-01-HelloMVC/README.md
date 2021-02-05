# SpringMVC-01-HelloMVC

**什么是MVC**

- MVC是模型(Model)、视图(View)、控制器(Controller)的简写，是一种软件设计规范。
- 是将业务逻辑、数据、显示分离的方法来组织代码。
- MVC主要作用是**降低了视图与业务逻辑间的双向偶合**。
- MVC不是一种设计模式，**MVC是一种架构模式**。当然不同的MVC存在差异。

关于MVC架构的详细知识，可以去看我之前的博客

http://rainingapple.cn/2020/11/10/MVC-MVP-MVVM/

<!--more-->

**参考**

spring-webmvc官方文档

https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/web.html#spring-web

狂神说博客

https://blog.csdn.net/qq_33369905/article/details/105828924

**Github仓库**

https://github.com/rainingapple/Spring_Learn

## SpringMVC

Spring MVC是Spring Framework的一部分，是基于Java实现MVC的轻量级Web框架。

官方文档：https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/web.html#spring-web

###  Spring MVC的特点

- 轻量级，简单易学
- 高效 , 基于请求响应的MVC框架
- 与Spring兼容性好，无缝结合
- 约定优于配置
- 功能强大：**RESTful**、数据验证、格式化、本地化、主题等
- 简洁灵活

### DispatcherServlet

Spring的web框架围绕**DispatcherServlet**设计，DispatcherServlet的作用是将请求分发到不同的处理器。

Spring MVC框架像许多其他MVC框架一样, 以**请求**为驱动 , 围绕一个中心Servlet分派请求及提供其他功能

**DispatcherServlet**继承自传统的的**HttpServlet** 。

![640](https://picgo-mk.oss-cn-beijing.aliyuncs.com/20210204194735.png)

从Spring 2.5开始，使用Java 5或者以上版本的用户可以采用基于注解形式进行开发，十分简洁

### SpringMVC的执行原理

![640 (1)](https://picgo-mk.oss-cn-beijing.aliyuncs.com/20210204195105.png)

**这里转载的狂神的博客**

上图实线表示SpringMVC框架提供的技术，不需要开发者实现，虚线表示需要开发者实现。

**简要分析执行流程**

- DispatcherServlet表示前置控制器，是整个SpringMVC的控制中心。用户发出请求，DispatcherServlet接收请求并拦截请求。

  我们假设请求的url为 : http://localhost:8080/SpringMVC/hello 如上url拆分成三部分：

  - http://localhost:8080服务器域名与端口
  - SpringMVC部署在服务器上的web站点
  - hello表示控制器

  通过分析，如上url表示为：请求位于服务器localhost:8080上的SpringMVC站点的hello控制器。

- HandlerMapping为处理器映射。DispatcherServlet调用HandlerMapping,HandlerMapping根据请求url查找Handler。

- HandlerExecution表示具体的Handler,其主要作用是根据url查找控制器，如上url被查找控制器为：hello。

- HandlerExecution将解析后的信息传递给DispatcherServlet,如解析控制器映射等。

- HandlerAdapter表示处理器适配器，其按照特定的规则去执行Handler。

- Handler让具体的Controller执行。

- Controller将具体的执行信息返回给HandlerAdapter,如ModelAndView。

- HandlerAdapter将视图逻辑名或模型传递给DispatcherServlet。

- DispatcherServlet调用视图解析器(ViewResolver)来解析HandlerAdapter传递的逻辑视图名。

- 视图解析器将解析的逻辑视图名传给DispatcherServlet。

- DispatcherServlet根据视图解析器解析的视图结果，调用具体的视图。

- 最终视图呈现给用户。

![640 (2)](https://picgo-mk.oss-cn-beijing.aliyuncs.com/20210204195429.png)

## SpringMVC demo

**这个demo并不是最终的开发写法，由于讲解原理方便才采用这种实现**

**具体开发要更加简单，具体下一个模块：基于注解的开发**

### 配置MAVEN依赖

```xml
    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.1</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>5.2.12.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>servlet-api</artifactId>
            <version>2.5</version>
        </dependency>
        <dependency>
            <groupId>javax.servlet.jsp</groupId>
            <artifactId>jsp-api</artifactId>
            <version>2.1</version>
        </dependency>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>jstl</artifactId>
            <version>1.2</version>
        </dependency>
    </dependencies>
```

### Controller

```java
public class HelloController implements Controller {
    @Override
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView modelAndView = new ModelAndView();

        //只需要简单的配置即可设置视图信息与配置View名称
        modelAndView.addObject("msg","HelloMVC");
        modelAndView.setViewName("hello");

        return modelAndView;
    }
}
```

### View

这里使用简单的jsp页面，唯一的内容是显示msg标签中的储存的信息

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
${msg}
</body>
</html>
```

### 配置Dispatcher Servlet

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee https://jakarta.ee/xml/ns/jakartaee/web-app_5_0.xsd"
         version="5.0">

    <!--1.注册servlet-->
    <servlet>
        <servlet-name>SpringMVC</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!--通过初始化参数指定SpringMVC配置文件的位置，进行关联-->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:springmvc-servlet.xml</param-value>
        </init-param>
        <!-- 启动顺序，数字越小，启动越早 -->
        <load-on-startup>1</load-on-startup>
    </servlet>

    <!--所有请求都会被springmvc拦截 -->
    <servlet-mapping>
        <servlet-name>SpringMVC</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>

</web-app>
```

### 配置Spring Application context

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!--Spring处理映射器-->
    <bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"/>
    <!--Spring处理器适配器-->
    <bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter"/>

    <!--视图解析器:DispatcherServlet给他的ModelAndView-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" id="InternalResourceViewResolver">
        <!--配置前缀-->
        <property name="prefix" value="/WEB-INF/jsp/"/>
        <!--配置后缀-->
        <property name="suffix" value=".jsp"/>
    </bean>

    <!--配置对应的控制器-->
    <bean id="/hello" class="cn.rainingapple.controller.HelloController"></bean>
    
</beans>
```

### 配置Tomcat，测试

配置tomcat，使用 http://localhost:8080/hello 访问即可

这里需要注意的是，访问可能会出现 404 找不到 hello.jsp

我们需要额外将以来的包一起导入war包中，具体操作详见[狂神博客](http://mp.weixin.qq.com/s?__biz=Mzg2NTAzMTExNg%3D%3D&chksm=ce6104e9f9168dffb600f71f97f89c5581923948746b7e2e53a8f0287419ea9940e124c39b0e&idx=1&mid=2247483978&scene=21&sn=6711110a3b2595d6bb987ca02ee0a728#wechat_redirect)