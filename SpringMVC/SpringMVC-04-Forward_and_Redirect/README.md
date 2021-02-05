# SpringMVC-04-Forward_and_Redirect

SpringMVC 对基本的转发与重定向操作有了更好的封装

通过智能的处理器、适配器、视图解析器，我们可以方便的解析路径地址

并且，SpringMVC对原生的Servlet功能可以做到完全兼容

<!--more-->

**参考**

spring-webmvc官方文档

https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/web.html#spring-web

狂神说博客

https://blog.csdn.net/qq_33369905/article/details/105828924

**Github仓库**

https://github.com/rainingapple/Spring_Learn

## 兼容原始的ServletAPI

通过设置ServletAPI , 不需要视图解析器 .

- 通过 HttpServletResponse 进行**输出**
- 通过 HttpServletResponse **实现重定向**

​        注意原始的servlet，连发布的具体项目名都无法识别，重定向加上项目的前缀

- 通过 HttpServletRequest **实现转发**

​       转发使用的是相对于当前项目的全限定名

这显然非常容易出错，并且复用性差

```java
@Controller
public class Test_ServletAPI {

    @GetMapping("/servlet/test1")
    public void test1(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getWriter().print("采用resp的writer输出消息");
    }

    @GetMapping("servlet/test2")
    public void test2(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("/SpringMVC_04_Forward_and_Redirect_war_exploded/index.jsp");
    }

    @GetMapping("servlet/test3")
    public void test3(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.getRequestDispatcher("/index.jsp").forward(req,resp);
    }
}
```

## 无视图解析器的MVC

测试时我们将视图解析器去掉

每一次我们都需要使用相对于项目的完全限定名，Spring相比于Servlet智能多了

```java
@Controller
public class Test_MVC_NO_ViewResolver {

    @GetMapping("/MVCNO/test1")
    public String test1(){
        return "/WEB-INF/jsp/success.jsp";
    }

    @GetMapping("/MVCNO/test2")
    public String test2(){
        return "forward:/WEB-INF/jsp/success.jsp";
    }

    @GetMapping("/MVCNO/test3")
    public String test3(){
        return "redirect:/index.jsp";
    }
}
```

## 加入视图解析器的MVC

默认执行方式为转发，使用重定向直接使用  **redirect：**  即可

重定向没有像转发一样的视图解析器的路径拼合，重定向使用当前项目下的全限定名

```java
@Controller
public class Test_MVC_ViewResolver {

    @GetMapping("/MVC/test1")
    public String test1(){
        return "success";
    }

    @GetMapping("/MVC/test2")
    public String test2(){
        return "redirect:/index.jsp";
    }
}
```

