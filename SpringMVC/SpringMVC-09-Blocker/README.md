# SpringMVC-09-Blocker

SpringMVC的处理器拦截器类似于Servlet开发中的过滤器Filter,用于对处理器进行预处理和后处理。

**过滤器与拦截器的区别：**拦截器是AOP思想的具体应用。

**过滤器**

- servlet规范中的一部分，任何java web工程都可以使用
- 在url-pattern中配置了path之后，可以对要访问的资源进行拦截

**拦截器** 

- 拦截器是SpringMVC框架自己的，只有使用了SpringMVC框架的工程才能使用
- 拦截器只会拦截访问的**控制器方法**， 如果访问的是jsp/html/css/image/js是不会进行拦截的

<!--more-->

**参考**

spring-webmvc官方文档

https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/web.html#spring-web

狂神说博客

https://blog.csdn.net/qq_33369905/article/details/105828924

**Github仓库**

https://github.com/rainingapple/Spring_Learn

## HelloInterceptor

### 对应实现接口HandlerInterceptor

可选得需要实现的方法：preHandle、postHandle、afterCompletion

对应执行顺序为：执行前，执行后，执行结束后清理时

```java
public class HelloInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("处理前");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("处理后");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("清理中");
    }
}
```

### 配置 \<mvc:interceptors> 

```xml
<!--关于拦截器的配置-->
<mvc:interceptors>
   <mvc:interceptor>
       <!--/** 包括路径及其子路径-->
       <!--/admin/* 拦截的是/admin/add等等这种 , /admin/add/user不会被拦截-->
       <!--/admin/** 拦截的是/admin/下的所有-->
       <mvc:mapping path="/**"/>
       <!--bean配置的就是拦截器-->
       <bean class="cn.rainingapple.interceptor.HelloInterceptor"/>
   </mvc:interceptor>
</mvc:interceptors>
```

### 创建控制器测试

```java
@Controller
public class MyController {

   @RequestMapping("/test1")
   @ResponseBody
   public String testFunction() {
       System.out.println("控制器中的方法执行了");
       return "hello";
  }
}
```

## 使用拦截器实现登录验证

### View

#### index.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
  <h1>首页</h1>
  <a href="${pageContext.request.contextPath}/jumplogin">登录</a>
  </body>
</html>
```

#### login.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>

<h1>登录页面</h1>
<hr>

<body>
<form action="${pageContext.request.contextPath}/login">
    用户名：<input type="text" name="username"> <br>
    密码：<input type="password" name="pwd"> <br>
    <input type="submit" value="提交">
</form>
</body>
</html>
```

#### success.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<h1>登录成功页面</h1>
<hr>

${user}
<a href="${pageContext.request.contextPath}/logout">注销</a>
</body>
</html>
```

### Controller

```java
@Controller
public class MyController {

    @RequestMapping("/jumplogin")
    public String jumplogin(){
        System.out.println("测试中");
        return "redirect:/login.jsp";
    }

    @RequestMapping("/login")
    public String login(HttpSession httpSession,String username,String pwd){
        httpSession.setAttribute("username",username);
        httpSession.setAttribute("pwd",pwd);
        return "success";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession httpSession){
        httpSession.invalidate();
        return "redirect:/login.jsp";
    }
}
```

### Interceptor

```java
public class LoginInterCeptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println(request.getRequestURI());
        if (request.getRequestURI().equals("/SpringMVC_09_Blocker_war_exploded/login")){
            System.out.println(1);
            if (request.getParameter("username").equals("admin")){
                System.out.println(2);
                if(request.getParameter("pwd").equals("123456")){
                    System.out.println(3);
                    return true;
                }
            }
        }
        return false;
    }
}
```

#### 配置 \<mvc:interceptors>

```xml
    <mvc:interceptors>
        <mvc:interceptor>
            <mvc:mapping path="/login"/>
            <bean class="cn.rainingapple.interceptor.LoginInterCeptor"/>
        </mvc:interceptor>
    </mvc:interceptors>
```

