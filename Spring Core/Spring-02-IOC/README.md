# Spring-02-IOC

IoC is also known as dependency injection (DI). 

It is a process whereby objects define their dependencies (that is, the other objects they work with) only through constructor arguments, arguments to a factory method, or properties that are set on the object instance after it is constructed or returned from a factory method.The container then injects those dependencies when it creates the bean. 

This process is fundamentally the inverse of the bean itself controlling the instantiation or location of its dependencies by using direct construction of classes or a mechanism such as the Service Locator pattern.

**参考**

Spring Core官方文档

https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/core.html#beans

狂神说博客

https://www.cnblogs.com/renxuw/p/12994080.html

https://mp.weixin.qq.com/s/VM6INdNB_hNfXCMq3UZgTQ

**Github仓库**

https://github.com/rainingapple/Spring_Learn

## 传统编码方式

### UserDao接口

```java
public interface UserDao {
   public void getUser();
}
```

### Dao实现类

```java
public class UserDaoImpl implements UserDao {
   @Override
   public void getUser() {
       System.out.println("获取用户数据");
  }
}

public class UserDaoImpl_another implements UserDao {
   @Override
   public void getUser() {
       System.out.println("la获取用户数据la");
  }
}
```

### UserService的接口

```java
public interface UserService {
   public void getUser();
}
```

### Service实现类

```java
public class UserServiceImpl implements UserService {
   private UserDao userDao = new UserDaoImpl();

   @Override
   public void getUser() {
       userDao.getUser();
  }
}
```

### 测试代码

```java
@Test
public void test(){
   UserService service = new UserServiceImpl();
   service.getUser();
}
```

### 传统方式的困难

**这样所有的编码都是写死的，加入我们此时要把Dao的实现换成另一种方式，那么需要程序员从源码级进行修改**

**这样的特性显然不能满足用户的定制化需求**

## 解决方案 

我们可以在需要用到他的地方 , 不去实现它 , 而是留出一个接口set , 实现动态设置，具体做出如下修改

### 新的实现类

```java
public class UserServiceImpl implements UserService {
   private UserDao userDao;

   public void setUserDao(UserDao userDao) {
       this.userDao = userDao;
  }

   @Override
   public void getUser() {
       userDao.getUser();
  }
}
```

### 新的测试类

```java
@Test
public void test(){
   UserServiceImpl service = new UserServiceImpl();
   service.setUserDao( new UserDaoMySqlImpl() );
   service.getUser();
}
```

**仔细去思考一下 , 以前所有东西都是由程序去进行控制创建 , 而现在是由我们自行控制创建对象 ,**

**主动权交给了调用者 ，程序不用去管怎么创建,怎么实现了， 它只负责提供一个接口 .**

**更进一步想，如果可以使用配置文件加set，是不是可以实现更进一步的解耦呢？**

## Spring IOC

### IOC(Inversion of Control)

一般我们使用面向对象编程 , 对象的创建与对象间的依赖关系完全硬编码在程序中，对象的创建由程序自己控制

控制反转后将对象的创建转移给第三方，即获得依赖对象的方式反转了。

**可以说，控制反转是一种通过描述（XML或注解）并通过第三方去生产或获取特定对象的方式。**

**在Spring中实现控制反转的是IoC容器，其实现方法是依赖注入（Dependency Injection,DI）。**

![640 (1)](https://picgo-mk.oss-cn-beijing.aliyuncs.com/20210126200216.png)

### Spring配置

可以使用XML配置，也可以使用注解，新版本的Spring也可以零配置实现IOC。

### Spring简易工作流程

- 容器在初始化时先读取配置文件
- 根据配置文件或元数据创建与组织对象存入容器中
- 程序使用时再从Ioc容器中取出需要的对象。

![640](https://picgo-mk.oss-cn-beijing.aliyuncs.com/20210126200219.png)

- 采用XML方式配置Bean的时候，Bean的定义信息是和实现分离的，
- 采用注解的方式可以把两者合一，Bean的定义信息直接以注解的形式定义在实现类中，从而达到了零配置的目的。

