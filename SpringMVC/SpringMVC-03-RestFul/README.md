# SpringMVC-03-RestFul

## RestFul 风格

### 概念

Restful就是一个资源定位及资源操作的风格。不是标准也不是协议，只是一种风格。

基于这个风格设计的软件可以更简洁，更有层次，更易于实现缓存等机制。

### 传统方式操作资源 

通过不同的**参数**来实现不同的效果

​	http://127.0.0.1/item/queryItem.action?id=1 查询,GET

​	http://127.0.0.1/item/saveItem.action 新增,POST

​	http://127.0.0.1/item/updateItem.action 更新,POST

​	http://127.0.0.1/item/deleteItem.action?id=1 删除,GET或POST

### 使用RESTful操作资源

可以通过不同的**请求方式**来实现不同的效

请求地址一样，但是功能可以不同

- http://127.0.0.1/item/1 查询,GET
- http://127.0.0.1/item 新增,POST
- http://127.0.0.1/item 更新,PUT
- http://127.0.0.1/item/1 删除,DELETE

**那么RestFul在SpringMVC中如何实现呢？**

<!--more-->

**参考**

spring-webmvc官方文档

https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/web.html#spring-web

狂神说博客

https://blog.csdn.net/qq_33369905/article/details/105828924

**Github仓库**

https://github.com/rainingapple/Spring_Learn

## 传统风格

```java
@Controller
public class RestFulController {

    @RequestMapping("/rest1")
    public String test1(int a, int b, Model model){
        model.addAttribute("msg",a+b);
        return "test";
    }

}
```

使用类似 http://localhost:8080/SpringMVC_03_RestFul_war_exploded/rest1?a=2&b=3 方式传参

## RestFul风格

### 使用@PathVariable

```java
@Controller
public class RestFulController {

    @RequestMapping("/rest1/{a}/{b}")
    public String test1(@PathVariable int a, @PathVariable int b, Model model){
        model.addAttribute("msg",a+b);
        return "test";
    }

}
```

使用类似 http://localhost:8080/SpringMVC_03_RestFul_war_exploded/rest1/5/6 方式传参

#### 使用路径变量的好处

- 使请求变得更加简洁
- 获得参数更加方便
- 通过路径变量的类型可以约束访问参数，如果类型不一样，则访问不到对应的请求方法。
- 更加安全，用户不用再知道变量名，可以传参数

### 使用method约束请求类型

**设定格式为RequestMethod.POST**

```java
@Controller
public class RestFulController {

    @RequestMapping(value = "/rest1/{a}/{b}",method = {RequestMethod.POST})
    public String test1(@PathVariable int a, @PathVariable int b, Model model){
        model.addAttribute("msg",a+b);
        return "test";
    }

}
```

**设定格式为RequestMethod.GET**

```java
@Controller
public class RestFulController {

    @RequestMapping(value = "/rest1/{a}/{b}",method = {RequestMethod.GET})
    public String test1(@PathVariable int a, @PathVariable int b, Model model){
        model.addAttribute("msg",a+b);
        return "test";
    }

}
```

只有请求格式与约定一致时才成功访问，由此可以实现同一个请求，方法不同页面不同

### 使用组合注解约束请求类型

**设定注解为PostMapping**

```java
@Controller
public class RestFulController {

    @PostMapping("/rest1/{a}/{b}")
    public String test1(@PathVariable int a, @PathVariable int b, Model model){
        model.addAttribute("msg",a+b);
        return "test";
    }

}
```

**设定注解为GETMapping**

```java
@Controller
public class RestFulController {

    @GETMapping("/rest1/{a}/{b}")
    public String test1(@PathVariable int a, @PathVariable int b, Model model){
        model.addAttribute("msg",a+b);
        return "test";
    }

}
```

同样，只有请求格式与约定的GET一致时才成功访问