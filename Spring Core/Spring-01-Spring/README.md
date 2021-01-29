# Spring-01-Spring

## Spring 简介

### 历史

- 2002年，首次推出Spring框架的雏形：interface21，官网：https://interface21.io/
- Spring以interface21为基础，于2004年3月24日发布1.0版本
- Rod Johnson，Spring Framework创始人。 Rod在悉尼大学不仅获得了计算机学位，同时还获得了音乐学位。更令人吃惊的是在回到软件开发领域之前，他还获得了音乐学的博士学位。 有着相当丰富的C/C++技术背景的Rod早在1996年就开始了对Java服务器端技术的研究。

### SSH与SSM

- SSH：Struct2+Spring+Hibernate
- SSM：SpringMVC+Spring+MyBatis

### 官方地址

官网地址：https://spring.io/

仓库地址：https://repo.spring.io/release/org/springframework/spring/

github：https://github.com/spring-projects/spring-framework

### Maven导包

#### Spring Web MVC

```xml
<!-- https://mvnrepository.com/artifact/org.springframework/spring-webmvc -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>5.2.12.RELEASE</version>
</dependency>
```

#### Spring JDBC

```xml
<!-- https://mvnrepository.com/artifact/org.springframework/spring-jdbc -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>5.2.12.RELEASE</version>
</dependency>
```

### 优点

- 开源免费的容器
- 轻量级、非入侵式的框架
- 控制反转（IOC）、面向切面编程（AOP）
- 支持事务处理、良好的框架支持

## Spring 基本组成

![1219227-20170930225010356-45057485](https://picgo-mk.oss-cn-beijing.aliyuncs.com/20210126154246.gif)

### 核心容器（Spring Core）

- 核心容器提供Spring框架的基本功能。
- Spring以bean的方式组织和管理Java应用中的各个组件及其关系。
- Spring使用BeanFactory来产生和管理Bean，它是工厂模式的实现。
- BeanFactory使用**控制反转**(IoC)模式将**应用的配置**和**依赖性规范**与实际的应用程序代码分开。

### 应用上下文（Spring Context）

- Spring Context是一个配置文件，向Spring框架提供上下文信息。
- Spring上下文包括企业服务，如JNDI、EJB、电子邮件、国际化、校验和调度功能。

### Spring面向切面编程（Spring AOP）

- 通过配置管理特性，Spring AOP 模块直接将面向切面的编程功能集成到了 Spring框架中。
- Spring AOP 模块为基于 Spring 的应用程序中的对象提供了事务管理服务。
- 通过使用 Spring AOP，不用依赖 EJB 组件，就可以将**声明性事务**管理集成到应用程序中。

### JDBC和DAO模块（Spring DAO）

- 提供了有意义的异常层次结构来管理异常处理，处理数据库供应商所抛出的错误信息。
- 简化了错误处理，极大的降低了代码量。

### 对象实体映射（Spring ORM）

- Spring框架插入了若干个ORM框架，从而提供了ORM对象的关系工具，其中包括了Hibernate、JDO和 IBatis SQL Map等
- 这些遵从Spring的通用事务和DAO异常层次结构。

### Web模块（Spring Web）

- Web上下文模块建立在应用程序上下文模块之上，为基于web的应用程序提供了上下文。
- Spring框架支持与Struts集成
- 简化了处理多部分请求以及将请求参数绑定到域对象的工作。

### MVC模块（Spring Web MVC）

- MVC框架是一个全功能的构建Web应用程序的MVC实现。
- Spring框架的功能可以用在任何J2EE服务器当中，大多数功能也适用于不受管理的环境。
- Spring的核心要点就是支持不绑定到特定J2EE服务的可重用业务和数据的访问的对象，方便测试与重用

















