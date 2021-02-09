# Spring-09-整合MyBatis

**什么是 MyBatis-Spring？**

MyBatis-Spring 会帮助你将 MyBatis 代码无缝地整合到 Spring 中。

**知识基础**

在开始使用 MyBatis-Spring 之前，你需要先熟悉 Spring 和 MyBatis 这两个框架和有关它们的术语。这很重要

MyBatis-Spring 需要以下版本：

| MyBatis-Spring | MyBatis | Spring 框架 | Spring Batch | Java    |
| :------------- | :------ | :---------- | :----------- | :------ |
| 2.0            | 3.5+    | 5.0+        | 4.0+         | Java 8+ |
| 1.3            | 3.4+    | 3.2.2+      | 2.1+         | Java 6+ |

- 在基础的 MyBatis 用法中，是通过 SqlSessionFactoryBuilder 来创建 SqlSessionFactory 的。

​       而在 MyBatis-Spring 中，则使用 SqlSessionFactoryBean 来创建。

- 在基础的 MyBatis 用法中，是通过 SqlSession来执行映射了的语句，提交或回滚连接

  在 MyBatis 中，你可以使用 SqlSessionFactory 来创建 SqlSession。

<!--more-->

**参考**

mybatis-spring官方文档

http://mybatis.org/spring/zh/sqlsession.html

狂神说博客

https://www.cnblogs.com/renxuw/p/12994080.html

https://mp.weixin.qq.com/s/gXFMNU83_7PqTkNZUgvigA

**Github仓库**

https://github.com/rainingapple/Spring_Learn

## Spring-Mybatis

**Mybatis 基础内容详见我之前的日志**

### 导入相关依赖

```xml
<dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.1</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.6</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.springframework/spring-jdbc -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>5.2.12.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.9.6</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.mybatis/mybatis-spring -->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>2.0.6</version>
        </dependency>

    </dependencies>
```

### 配置静态资源过滤

根据Maven的默认约定，不导出非资源目录下的资源文件。

配置静态资源过滤以解决资源无法导出的问题

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

### 配置Application context

#### 配置数据源

```xml
   <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
        <property name="url" value="jdbc:mysql://****/mybatis?
                                    useSSL=false&amp;
                                    useUnicode=true&amp;
                                    characterEncoding=utf8"></property>
        <property name="username" value="root"></property>
        <property name="password" value="@appleis555"></property>
    </bean>
```

这一部分可以等效替换掉以下部分

```xml
           <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://你的url?
                useSSL=false&amp;
                useUnicode=true&amp;
                characterEncoding=utf8"/>
                <property name="username" value="root"/>
                <property name="password" value="你的密码"/>
            </dataSource>
```

#### 配置sqlSessionFactory

- SqlSessionFactory有一个唯一的必要属性：用于 JDBC 的 DataSource。

​       这可以是任意的 DataSource 对象，它的配置方法和其它 Spring 数据库连接是一样的。

- 一个常用的属性是 configLocation，它用来指定 MyBatis 的 XML 配置文件路径。它在需要修改 MyBatis 的基础配置非常有用。

​        通常，基础配置指的是 < settings> 或 < typeAliases>元素。

- 需要注意的是，这个配置文件并不需要是一个完整的 MyBatis 配置。

```xml
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <!--引入原来的mybatis文件，这步是可选的-->
        <property name="configLocation" value="mybatis-config.xml"></property>
        <property name="dataSource" ref="dataSource"></property>
        <property name="mapperLocations" value="cn/rainingapple/dao/*.xml"></property>
    </bean>
```

这一部分可以等效替换掉以下两部分

```xml
    <mappers>
        <mapper resource="cn/rainingapple/dao/UserMapper.xml"></mapper>
    </mappers>
```

```java
String resource = "mybatis-config.xml";
InputStream inputStream = Resources.getResourceAsStream(resource);
sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
```

#### 配置sqlSession

- SqlSessionTemplate 是 MyBatis-Spring 的核心。
- 作为 SqlSession 的一个实现，这意味着可以使用它无缝代替你代码中已经在使用的 SqlSession。
- 模板可以参与到 Spring 的事务管理中，并且由于其是线程安全的，可以供多个映射器类使用，
- 你应该总是用 SqlSessionTemplate 来替换 MyBatis 默认的 DefaultSqlSession 实现。
- 在同一应用程序中的不同类之间混杂使用可能会引起数据一致性的问题。

```xml
    <bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
        <constructor-arg name="sqlSessionFactory" ref="sqlSessionFactory"></constructor-arg>
    </bean>
```

这一部分可以等效替换掉以下部分

```java
public static SqlSession getSession(){
    return sqlSessionFactory.openSession();
}
```

**UserMapper与UserMapper.xml暂时还是需要手动配置**

#### 配置UserMapper接口

```java
public interface UserMapper {
    List<User> selectuser();
}
```

#### 配置UserMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="cn.rainingapple.dao.UserMapper">
    <select id="selectuser" resultType="user">
        select * from user
    </select>
</mapper>
```

#### 配置UserMapperImpl

##### xml

```xml
    <bean id="UserDao" class="cn.rainingapple.dao.UserMapperImpl">
        <property name="sqlSessionTemplate" ref="sqlSession"></property>
    </bean>
```

##### UserMapperImpl

```java
public class UserMapperImpl implements UserMapper{

    private SqlSessionTemplate sqlSessionTemplate;

    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public List<User> selectuser() {
        UserMapper mapper = sqlSessionTemplate.getMapper(UserMapper.class);
        return mapper.selectuser();
    }
}
```

这一部分可以等效替换掉以下部分

```xml
<mapper namespace="cn.rainingapple.dao.UserMapper">
    <select id="selectUser" resultType="cn.rainingapple.pojo.User">
        select * from user
    </select>
</mapper>
```

#### 完整的spring-mybatis.xml

可复用的配置数据源、配置sessionfactory、配置sessiontemplate在这里完成

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">
    
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        
        <property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
        <property name="url" value="jdbc:mysql://rm-bp1y5p52ds3x2u07sfo.mysql.rds.aliyuncs.com:3306/mybatis?useSSL=false&amp;useUnicode=true&amp;characterEncoding=utf8"></property>
        <property name="username" value="root"></property>
        <property name="password" value="@appleis555"></property>
    </bean>

    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <!--引入原来的mybatis文件，这步是可选的-->
        <property name="configLocation" value="mybatis-config.xml"></property>
        <property name="dataSource" ref="dataSource"></property>
        <property name="mapperLocations" value="cn/rainingapple/dao/*.xml"></property>
    </bean>

    <bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
        <constructor-arg name="sqlSessionFactory" ref="sqlSessionFactory"></constructor-arg>
    </bean>
</beans>
```

#### 合并的beans.xml

通过引入spring-mybatis导入共同的相关操作，在beans.xml中仅需要配置UserMapperImpl,实现配置复用

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <import resource="spring-mybatis.xml"></import>

    <bean id="UserDao" class="cn.rainingapple.dao.UserMapperImpl">
        <property name="sqlSessionTemplate" ref="sqlSession"></property>
    </bean>
</beans>
```

#### 删减后的mybatis-config.xml

可以完全删掉，也可以在这里做少量配置。

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <typeAliases>
        <package name="cn.rainingapple.pojo"/>
    </typeAliases>
</configuration>
```

### MyTest

```java
public class MyTest {
    @Test
    public void testselectuser(){
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        //相当于在这里直接获得了实现类
        UserMapper userDao = context.getBean("UserDao", UserMapper.class);
        List<User> selectuser = userDao.selectuser();
        System.out.println(selectuser);
    }
}
```

### 扩展-使用SqlSessionDaoSupport

`SqlSessionDaoSupport` 是一个抽象的支持类，用来为你提供 `SqlSession`。调用 `getSqlSession()` 方法你会得到一个 `SqlSessionTemplate`，之后可以用于执行 SQL 方法**这样可以省去SqlSessionTemplate的配置**

#### UserMapperImpl2

```java
public class UserMapperImpl2 extends SqlSessionDaoSupport implements UserMapper{
    @Override
    public List<User> selectuser() {
        return getSqlSession().getMapper(UserMapper.class).selectuser();
    }
}
```

#### beans.xml

```java
    <bean id="UserDao2" class="cn.rainingapple.dao.UserMapperImpl2">
        <!--这里是父类set需要使用的-->
        <property name="sqlSessionFactory" ref="sqlSessionFactory"></property>
    </bean>
```

#### MyTest

```java
    @Test
    public void testselectuser(){
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        UserMapper userDao = context.getBean("UserDao2", UserMapper.class);
        List<User> selectuser = userDao.selectuser();
        System.out.println(selectuser);
    }
```

