# SpringBoot-03-Web

关于SpringBoot解决Web应用基本配置中的：

- 静态资源过滤与导出
- 首页引导页设置
- 模板引擎（thymeleaf）

<!--more-->

## 静态资源过滤

对这个配置溯源，我们可以追溯到 WebFluxConfig 中的 addResourceHandlers 方法

```java
@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
   if (!this.resourceProperties.isAddMappings()) {
      logger.debug("Default resource handling disabled");
      return;
   }
   if (!registry.hasMappingForPattern("/webjars/**")) {
      ResourceHandlerRegistration registration = registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");
      configureResourceCaching(registration);
      customizeResourceHandlerRegistration(registration);
   }
   String staticPathPattern = this.webFluxProperties.getStaticPathPattern();
   if (!registry.hasMappingForPattern(staticPathPattern)) {
      ResourceHandlerRegistration registration = registry.addResourceHandler(staticPathPattern)
            .addResourceLocations(this.resourceProperties.getStaticLocations());
      configureResourceCaching(registration);
      customizeResourceHandlerRegistration(registration);
   }
}
```

也就是在资源过滤这个问题上有三层判断

**如果我们设置了自定义的静态资源过滤的路径，那么走默认的**

**如果配置了webjars，那么会把 /META-INF/resources/webjars下的文件映射到/webjars/**。**

比如我们加入jquery的依赖

```xml
<dependency>
   <groupId>org.webjars</groupId>
   <artifactId>jquery</artifactId>
   <version>3.5.1</version>
</dependency>
```

对应使用：http://localhost:8080/webjars/jquery/3.5.1/jquery.js 访问

**我们可以在对应的resources下新建resources，static，public目录来存放静态资源**

对应使用：http://localhost:8080/1.js 来访问

## 首页处理

同样的我们全局搜索welcomePageHandlerMapping，进行溯源的话也可以找到CLASSPATH_RESOURCE_LOCATIONS常量。

对应的和上面的静态资源过滤地址一样

```java
private static final String[] CLASSPATH_RESOURCE_LOCATIONS = { "classpath:/META-INF/resources/",
				"classpath:/resources/", "classpath:/static/", "classpath:/public/" };
```

在对应的任意静态资源文件夹新建一个index.html就会自动被映射成首页

## 模板引擎

由于jsp的职责不单一，业界基本已经舍弃了这项技术。SpringBoot默认不支持jsp。而不支持jsp，如果我们直接用纯静态页面的方式，那给我们开发会带来非常大的麻烦，那怎么办呢？

**SpringBoot推荐使用模板引擎**

模板引擎，我们其实大家听到很多，其实jsp就是一个模板引擎，还有用的比较多的freemarker，包括SpringBoot给我们推荐的Thymeleaf，模板引擎有非常多，但再多的模板引擎，他们的思想都是一样的，什么样一个思想呢我们来看一下这张图：

![640](https://picgo-mk.oss-cn-beijing.aliyuncs.com/20210217172158.png)

模板引擎的作用就是我们来写一个页面模板，包含一些表达式。然后把这个模板和这个数据交给我们模板引擎，模板引擎按照我们这个数据帮你把这表达式解析、填充到我们指定的位置，然后把这个数据最终生成一个我们想要的内容给我们写出去，这就是我们这个模板引擎。不管是jsp还是其他模板引擎，都是这个思想。只不过不同模板引擎之间，语法不一样。

由于thymeleaf语法的便捷性与通用性，成为SpringBoot中广泛使用的引擎。

### 导入依赖

我们可以在新建项目时直接勾选，也可以后前添加依赖或者手动导入

```xml
<!--thymeleaf-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

### 名称解析

对应打开 ThymeleafProperties 类，发现对应的Thymelleaf解析的时templates/*.html

```java
@ConfigurationProperties(prefix = "spring.thymeleaf")
public class ThymeleafProperties {

	private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

	public static final String DEFAULT_PREFIX = "classpath:/templates/";

	public static final String DEFAULT_SUFFIX = ".html";
```

### demo

#### controller

```java
@Controller
public class ThyController {

    @RequestMapping("test1")
    public String test2(Model model){
        model.addAttribute("msg","测试一");
        return "test1";
    }
}
```

#### test1.html

对应在resources/templates下新建，引入命名空间 xmlns:th="http://www.thymeleaf.org"

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<div th:text="${msg}"></div>
</body>
</html>
```

#### thymeleaf 简单语法

```
Simple expressions:（表达式语法）
Variable Expressions: ${...}：获取变量值；OGNL；
    1）、获取对象的属性、调用方法
    2）、使用内置的基本对象：#18
         #ctx : the context object.
         #vars: the context variables.
         #locale : the context locale.
         #request : (only in Web Contexts) the HttpServletRequest object.
         #response : (only in Web Contexts) the HttpServletResponse object.
         #session : (only in Web Contexts) the HttpSession object.
         #servletContext : (only in Web Contexts) the ServletContext object.

    3）、内置的一些工具对象：
　　　　　　#execInfo : information about the template being processed.
　　　　　　#uris : methods for escaping parts of URLs/URIs
　　　　　　#conversions : methods for executing the configured conversion service (if any).
　　　　　　#dates : methods for java.util.Date objects: formatting, component extraction, etc.
　　　　　　#calendars : analogous to #dates , but for java.util.Calendar objects.
　　　　　　#numbers : methods for formatting numeric objects.
　　　　　　#strings : methods for String objects: contains, startsWith, prepending/appending, etc.
　　　　　　#objects : methods for objects in general.
　　　　　　#bools : methods for boolean evaluation.
　　　　　　#arrays : methods for arrays.
　　　　　　#lists : methods for lists.
　　　　　　#sets : methods for sets.
　　　　　　#maps : methods for maps.
　　　　　　#aggregates : methods for creating aggregates on arrays or collections.
==================================================================================

  Selection Variable Expressions: *{...}：选择表达式：和${}在功能上是一样；
  Message Expressions: #{...}：获取国际化内容
  Link URL Expressions: @{...}：定义URL；
  Fragment Expressions: ~{...}：片段引用表达式

Literals（字面量）
      Text literals: 'one text' , 'Another one!' ,…
      Number literals: 0 , 34 , 3.0 , 12.3 ,…
      Boolean literals: true , false
      Null literal: null
      Literal tokens: one , sometext , main ,…
      
Text operations:（文本操作）
    String concatenation: +
    Literal substitutions: |The name is ${name}|
    
Arithmetic operations:（数学运算）
    Binary operators: + , - , * , / , %
    Minus sign (unary operator): -
    
Boolean operations:（布尔运算）
    Binary operators: and , or
    Boolean negation (unary operator): ! , not
    
Comparisons and equality:（比较运算）
    Comparators: > , < , >= , <= ( gt , lt , ge , le )
    Equality operators: == , != ( eq , ne )
    
Conditional operators:条件运算（三元运算符）
    If-then: (if) ? (then)
    If-then-else: (if) ? (then) : (else)
    Default: (value) ?: (defaultvalue)
    
Special tokens:
    No-Operation: _
```

##### 简单测试一下循环

###### controller

```java
    @RequestMapping("/test2")
    public String test2(Model model){
        model.addAttribute("str","<h1>测试二</h1>");
        model.addAttribute("stus", Arrays.asList("张三","李四"));
        return "test2";
    }
```

###### test2.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<div th:text="${str}"></div>
<div th:utext="${str}"></div>
<div th:each="stu:${stus}" th:text="${stu}"></div>
</body>
</html>
```

## 参考

springboot官方文档

https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-developing-web-applications

狂神说博客

https://blog.csdn.net/qq_33369905/article/details/105828924

## Github仓库

https://github.com/rainingapple/Spring_Learn

