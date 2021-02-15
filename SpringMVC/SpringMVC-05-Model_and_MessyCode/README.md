# SpringMVC-05-Model_and_MessyCode

- MVC中我们需要解决请求行的参数解析问题，具体的我们需要规定参数的名称、是否必须、以及必要的出错处理
- MVC中我们需要使用怎样的格式进行数据回传，Model、ModelAndView、ModelMap有何区别
- MVC依旧逃避不了web经常遇见的乱码问题，所幸MVC提供了比原生WEB更好的解决方式

<!--more-->

**参考**

spring-webmvc官方文档

https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/web.html#spring-web

狂神说博客

https://blog.csdn.net/qq_33369905/article/details/105828924

**Github仓库**

https://github.com/rainingapple/Spring_Learn

## 请求行数据处理

### 提交的域名称和处理方法的参数名一致

```java
    @RequestMapping("/test1")
    public String test1(String name){
        System.out.println(name);
        return "test";
    }
```

当我们传递name参数，对应传递成功并打印name。如果不传参或者传递错误的名字，返回null

### @RequestParam

当我们需要特殊约束必须传递某个参数时，可以使用@RequestParam

```java
    @RequestMapping("/test2")
    public String test2(@RequestParam("username") String username){
        System.out.println(username);
        return "test";
    }
```

username必须传递，否则报错

### 提交对象

#### 对应构造实体类User

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    int id;
    String name;
    int age;
}
```

三个注解分别提供setget等方法、无参构造、有参构造

#### 使用注解应引入lombok依赖

```xml
        <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.12</version>
        </dependency>
```

#### 测试

```java
    @RequestMapping("/test3")
    public String test3(User user){
        System.out.println(user);
        return "test";
    }
```

此时传递三个参数会对应注入到实体类对应的名称下，没有传递的初始值对应设置为java默认初始值

如果此时引入@RequestParam，则必须以对象的形式传参

## 数据回显

### 第一种 : 通过ModelAndView

实现controller接口一般用这个，我们前面一直都是如此 . 就不过多解释

```java
public class ControllerTest1 implements Controller {

   public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
       //返回一个模型视图对象
       ModelAndView mv = new ModelAndView();
       mv.addObject("msg","ControllerTest1");
       mv.setViewName("test");
       return mv;
  }
}
```

### 第二种 : 通过ModelMap

ModelMap是LinkedHashMap的子类，相比于Model多了HashMap相关的功能

```java
@RequestMapping("/test5")
public String hello(@RequestParam("username") String name, ModelMap model){
   //封装要显示到视图中的数据
   //相当于req.setAttribute("name",name);
   model.addAttribute("name",name);
   System.out.println(name);
   return "hello";
}
```

### 第三种 : 通过Model

```java
@RequestMapping("/test6")
public String hello(@RequestParam("username") String name, Model model){
   //封装要显示到视图中的数据
   //相当于req.setAttribute("name",name);
   model.addAttribute("msg",name);
   System.out.println(name);
   return "test";
}
```

### 对比

- Model 只有寥寥几个方法只适合用于储存数据，简化了新手对于Model对象的操作和理解；

- ModelMap 继承了 LinkedHashMap ，除了实现了自身的一些方法，同样的继承 LinkedMap 的方法和特性；

- ModelAndView 可以在储存数据的同时，可以进行设置返回的逻辑视图，进行控制展示层的跳转。

## 处理乱码

如果是url传参，在MVC中本来就不会乱码。但是其他请求依旧需要过滤器，比如表单提交

### 创建一个提交表单

注意表单提交走的重定向，注意这个地址要把项目名也加上。

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
  <form action="/SpringMVC_05_Model_and_MessyCode_war_exploded/test5" method="post">
    <input type="text" name="name">
    <input type="submit">
  </form>
  </body>
</html>
```

### 对应的Controller

```java
    @RequestMapping("/test5")
    public String test1(Model model, String name){
        model.addAttribute("msg",name);
        return "test";
    }
```

此时如果通过表单提交中文会产生乱码

### 使用MVC的CharacterEncodingFilter

我们可以自己写一个filter，自己重写dofilter，但是MVC中已经提供了很好的封装

```xml
    <filter>
        <filter-name>encoding</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>utf-8</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>encoding</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
```

绝大多数乱码问题都会解决。如果依旧产生乱码，可能浏览器或者系统的默认编码方式有问题

具体可以参考[狂神说SpringMVC04：数据处理及跳转](http://mp.weixin.qq.com/s?__biz=Mzg2NTAzMTExNg%3D%3D&chksm=ce6104fdf9168deb32664243023d374b336f2a4260b55846b533a8be70d2f9bc97e45f4ede47&idx=1&mid=2247483998&scene=21&sn=97c417a2c1484d694c761a2ad27f217d#wechat_redirect)

