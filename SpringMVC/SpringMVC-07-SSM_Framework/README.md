# SpringMVC-07-SSM_Framework

SSM（Spring+SpringMVC+MyBatis）三个框架到现在已基本有所了解

这篇博客记录SSM框架的初步整合

创建一个小型的图书管理系统

**环境**

- MySQL 5.7.*
- Tomcat 9.0.37
- Maven 3.6.3

<!--more-->

**参考**

狂神说博客

https://blog.csdn.net/qq_33369905/article/details/105828924

**Github仓库**

https://github.com/rainingapple/Spring_Learn

## 创建数据库

```sql
CREATE DATABASE `ssmbuild`;

USE `ssmbuild`;

CREATE TABLE `books`(
`bookID` INT(10) NOT NULL AUTO_INCREMENT COMMENT '书id',
`bookName` VARCHAR(100) NOT NULL COMMENT '书名',
`bookCounts` INT(11) NOT NULL COMMENT '数量',
`detail` VARCHAR(200) NOT NULL COMMENT '描述',
KEY `bookID` (`bookID`)
)ENGINE=INNODB DEFAULT CHARSET=utf8

INSERT INTO `books`(`bookID`,`bookName`,`bookCounts`,`detail`)VALUES
(1,'Java',1,'从入门到放弃'),
(2,'MySQL',10,'从删库到跑路'),
(3,'Linux',5,'从进门到进牢');
```

## 配置MAVEN

### 在整个MVC的父项目之上需要引入有关数据库连接的相关依赖

完整项目见GitHub

```xml
<dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>5.2.12.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
        </dependency>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.6</version>
        </dependency>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>2.0.6</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/com.mchange/c3p0 -->
        <dependency>
            <groupId>com.mchange</groupId>
            <artifactId>c3p0</artifactId>
            <version>0.9.5.2</version>
        </dependency>
    </dependencies>
```

### 配置maven资源导出

```xml
    <build>
        <resources>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>false</filtering>
            </resource>
            <resource>
                <directory>src/main/resources</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>false</filtering>
            </resource>
        </resources>
    </build>
```

## 数据操作部分

### Dao层

在后面我们会配置org.mybatis.spring.mapper.MapperScannerConfigurer

无需实现类，仅通过Mapper.xml 与 Mapper接口即可自动配置mapper

#### BookMapper

```java
public interface BookMapper {

    int addBook(Books book);
    int deleteBookById(int id);
    int updateBook(Books books);
    Books queryBookById(int id);
    List<Books> selectbooks();
}
```

#### BookMapper.xml

一定注意，id要与Mapper接口一一对应

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="cn.rainingapple.dao.BookMapper">

    <!--一定注意这里id一个字都不能错-->
    <insert id="addBook" parameterType="books">
        insert into books (bookName,bookCounts,detail)
        values (#{bookName},#{bookCounts},#{detail})
    </insert>

    <!--根据id删除一个Book-->
    <delete id="deleteBookById" parameterType="int">
        delete from books where bookID=#{id}
    </delete>

    <update id="updateBook" parameterType="Books">
        update books
        set bookCounts = #{bookCounts},bookName = #{bookName},detail = #{detail}
        where bookID = #{bookID};
    </update>

    <select id="queryBookById" resultType="books">
        select *
        from books
        where bookID = #{id};
    </select>

    <select id="selectbooks" resultType="books">
        select * from books
    </select>
</mapper>
```

### Service层

#### BookService

```java
public interface BookService {
    int addBook(Books book);

    int deleteBookById(int id);

    int updateBook(Books books);

    Books queryBookById(int id);

    List<Books> queryAllBook();
}
```

#### BookServiceImpl

这里我使用的是@Component注解，配合后面要配的组件扫描

使用xml也行

```java
@Component("BookServiceImpl")
public class BookServiceImpl implements BookService{

    @Value("#{bookMapper}")
    private BookMapper bookMapper;

    public void setBookMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    @Override
    public int addBook(Books book) {
        return bookMapper.addBook(book);
    }

    @Override
    public int deleteBookById(int id) {
        return bookMapper.deleteBookById(id);
    }

    @Override
    public int updateBook(Books books) {
        return bookMapper.updateBook(books);
    }

    @Override
    public Books queryBookById(int id) {
        return bookMapper.queryBookById(id);
    }

    @Override
    public List<Books> queryAllBook() {
        return bookMapper.selectbooks();
    }
}
```

### MyBatis-Spring配置

向sping中注册mybatis

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd">



    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
        <property name="url" value="jdbc:mysql://****/ssmbuild?
                                 useSSL=false&amp;
                                 useUnicode=true&amp;
                                 characterEncoding=utf8"></property>
        <property name="username" value="root"></property>
        <property name="password" value="*****"></property>
    </bean>

    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="typeAliasesPackage" value="cn.rainingapple.pojo"></property>
        <property name="dataSource" ref="dataSource"></property>
        <property name="mapperLocations" value="classpath:cn/rainingapple/dao/*.xml"></property>
    </bean>

    <bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
        <constructor-arg name="sqlSessionFactory" ref="sqlSessionFactory"></constructor-arg>
    </bean>

    <!-- 配置事务管理器 -->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource" />
    </bean>

    <!--扫描dao层-->

    <!--配置扫描Dao接口包，动态实现Dao接口注入到spring容器中(无需Impl，由xml与接口即可生成) -->
    <!--其实在这里多少可以感觉到，其实我们可以使用接口加注解实现功能，完全摒弃xml配置-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <!-- 注入sqlSessionFactory -->
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
        <!-- 给出需要扫描Dao接口包 -->
        <property name="basePackage" value="cn.rainingapple.dao"/>
    </bean>

    <!-- 扫描service相关的bean -->
    <context:component-scan base-package="cn.rainingapple.service" />

</beans>
```

### MyTest

截至这里已经形成了完整的模块，单元测试以下相关功能

```java
public class MyTest {

    @Test
    public void testadduser(){
        ApplicationContext context = new ClassPathXmlApplicationContext("mybatis-spring.xml");
        BookService bookServiceImpl = context.getBean("BookServiceImpl", BookService.class);
        int addBook = bookServiceImpl.addBook(new Books(1, "测试跑路", 9, "啦啦啦啦"));
        System.out.println(addBook);
    }

    @Test
    public void testdeleteuser(){
        ApplicationContext context = new ClassPathXmlApplicationContext("mybatis-spring.xml");
        BookService bookServiceImpl = context.getBean("BookServiceImpl", BookService.class);
        int bookById = bookServiceImpl.deleteBookById(3);
        System.out.println(bookById);
    }

    @Test
    public void testupdateuser(){
        ApplicationContext context = new ClassPathXmlApplicationContext("mybatis-spring.xml");
        BookService bookServiceImpl = context.getBean("BookServiceImpl", BookService.class);
        int book = bookServiceImpl.updateBook(new Books(4, "测试跑路", 99, "省略"));
        System.out.println(book);
    }

    @Test
    public void testselectuserbyid(){
        ApplicationContext context = new ClassPathXmlApplicationContext("mybatis-spring.xml");
        BookService bookServiceImpl = context.getBean("BookServiceImpl", BookService.class);
        Books book = bookServiceImpl.queryBookById(2);
        System.out.println(book);
    }

    @Test
    public void testselectuser(){
        ApplicationContext context = new ClassPathXmlApplicationContext("mybatis-spring.xml");
        BookService bookServiceImpl = context.getBean("BookServiceImpl", BookService.class);
        List<Books> selectbooks = bookServiceImpl.queryAllBook();
        for (Books books : selectbooks) {
            System.out.println(books);
        }
    }
}
```

## 视图与控制器

### Controller层

#### BookController

```java
@Controller
public class BookController {

    @Autowired
    @Qualifier("BookServiceImpl")
    private BookService bookService;

    @RequestMapping("/listbooks")
    public String listbooks(Model model){
        List<Books> books = bookService.queryAllBook();
        model.addAttribute("books",books);
        return "listedbooks";
    }

    @RequestMapping("/addbook")
    public String addbook(Model model){
        return "addBook";
    }

    @RequestMapping("/add")
    public String add(Books book){
        System.out.println(book);
        bookService.addBook(book);
        return "redirect:/listbooks";
    }

    @RequestMapping("/updatebook")
    public String updatebook(Model model){
        return "updateBook";
    }

    @RequestMapping("/update")
    public String update(Model model,Books book){
        System.out.println(book);
        bookService.updateBook(book);
        Books books = bookService.queryBookById(book.getBookID());
        model.addAttribute("books", books);
        return "redirect:/listbooks";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable int id){
        bookService.deleteBookById(id);
        return "redirect:/listbooks";
    }
}
```

### View层

视图直接用的狂神的

#### index.jsp

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
  <title>首页</title>
  <style type="text/css">
    a {
      text-decoration: none;
      color: black;
      font-size: 18px;
    }
    h3 {
      width: 180px;
      height: 38px;
      margin: 100px auto;
      text-align: center;
      line-height: 38px;
      background: deepskyblue;
      border-radius: 4px;
    }
  </style>
</head>
<body>

<h3>
  <a href="${pageContext.request.contextPath}/listbooks">点击进入列表页</a>
</h3>
</body>
</html>
```

#### listedbooks.jsp

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>书籍列表</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- 引入 Bootstrap -->
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<div class="container">

    <div class="row clearfix">
        <div class="col-md-12 column">
            <div class="page-header">
                <h1>
                    <small>书籍列表 —— 显示所有书籍</small>
                </h1>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-4 column">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/addbook">新增</a>
        </div>
    </div>

    <div class="row clearfix">
        <div class="col-md-12 column">
            <table class="table table-hover table-striped">
                <thead>
                <tr>
                    <th>书籍编号</th>
                    <th>书籍名字</th>
                    <th>书籍数量</th>
                    <th>书籍详情</th>
                    <th>操作</th>
                </tr>
                </thead>

                <tbody>
                <c:forEach var="book" items="${requestScope.get('books')}">
                    <tr>
                        <td>${book.getBookID()}</td>
                        <td>${book.getBookName()}</td>
                        <td>${book.getBookCounts()}</td>
                        <td>${book.getDetail()}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/updatebook?bookID=${book.getBookID()}">更改</a> |
                            <a href="${pageContext.request.contextPath}/delete/${book.getBookID()}">删除</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
```

#### addBook.jsp

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>新增书籍</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- 引入 Bootstrap -->
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">

    <div class="row clearfix">
        <div class="col-md-12 column">
            <div class="page-header">
                <h1>
                    <small>新增书籍</small>
                </h1>
            </div>
        </div>
    </div>
    <form action="${pageContext.request.contextPath}/add" method="post">
        书籍名称：<input type="text" name="bookName"><br><br><br>
        书籍数量：<input type="text" name="bookCounts"><br><br><br>
        书籍详情：<input type="text" name="detail"><br><br><br>
        <input type="submit" value="添加">
    </form>

</div>
```

#### updateBook.jsp

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>修改信息</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- 引入 Bootstrap -->
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">

    <div class="row clearfix">
        <div class="col-md-12 column">
            <div class="page-header">
                <h1>
                    <small>修改信息</small>
                </h1>
            </div>
        </div>
    </div>

    <form action="${pageContext.request.contextPath}/update" method="post">
        <input type="hidden" name="bookID" value="${pageContext.request.getParameter("bookID")}"/>
        书籍名称：<input type="text" name="bookName" value="${book.getBookName()}"/>
        书籍数量：<input type="text" name="bookCounts" value="${book.getBookCounts()}"/>
        书籍详情：<input type="text" name="detail" value="${book.getDetail() }"/>
        <input type="submit" value="提交"/>
    </form>

</div>
```

### springmvc-servlet.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/mvc
       https://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <context:component-scan base-package="cn.rainingapple.controller"/>
    <mvc:default-servlet-handler />
    <mvc:annotation-driven />


    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"
          id="internalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/jsp/" />
        <property name="suffix" value=".jsp" />
    </bean>

</beans>
```

## Web

### beans.xml

使用beans.xml整合mybatis-spring.xml与springmvc-servlet.xml

### web.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee https://jakarta.ee/xml/ns/jakartaee/web-app_5_0.xsd"
         version="5.0">

    <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <!--注意这里相应的应该改为beans.xml-->
            <param-value>classpath:beans.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>

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
</web-app>
```

配置tomcat，运行测试即可。需要注意Maven打包方式设定为war可能出现错误。

推荐使用Project Structure—Artifact—添加—Web Application:Archive—from module

使用idea配置tomcat部署到云服务器可以参考下一篇博客