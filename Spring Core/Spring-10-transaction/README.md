# Spring-10-transaction

## 事务

- 事务在项目开发过程非常重要，涉及到数据的一致性的问题，不容马虎！
- 事务管理是企业级应用程序开发中必备技术，用来确保数据的完整性和一致性。

事务就是把一系列的动作当成一个独立的工作单元，这些动作要么全部完成，要么全部不起作用。

### ACID

#### 原子性（atomicity）

- 事务是原子性操作，由一系列动作组成，事务的原子性确保动作要么全部完成，要么完全不起作用

#### 一致性（consistency）

- 一旦所有事务动作完成，事务就要被提交。数据和资源处于一种满足业务规则的一致性状态中

#### 隔离性（isolation）

- 可能多个事务会同时处理相同的数据，因此每个事务都应该与其他事务隔离开来，防止数据损坏

#### 持久性（durability）

- 事务一旦完成，无论系统发生什么错误，结果都不会受到影响。通常情况下，事务的结果被写到持久化存储器中

<!--more-->

**参考**

mybatis-spring官方文档

http://mybatis.org/spring/zh/transactions.html

狂神说博客

https://www.cnblogs.com/renxuw/p/12994080.html

https://mp.weixin.qq.com/s/mYOBJdygHDcXPYBls7cxUA

**Github仓库**

https://github.com/rainingapple/Spring_Learn

## Spring 声明式事务

### 不满足ACID的情景

#### UserMapper

```java
public interface UserMapper {
    List<User> selectuser();

    int adduser(User user);

    int deleteuser(int id);
}
```

#### UserMapper.xml

这里故意制造了一个错误，将delete写成了deletes

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="cn.rainingapple.dao.UserMapper">
    <select id="selectuser" resultType="cn.rainingapple.pojo.User">
        select *
        from user;
    </select>

    <insert id="adduser" parameterType="cn.rainingapple.pojo.User">
        insert into user (id,name,pwd)
        values (#{id},#{name},#{pwd});
    </insert>

    <delete id="deleteuser" parameterType="_int">
        deletes from user where id = #{id}
    </delete>
</mapper>
```

#### UserMapperImpl

我们制造的情景是：插入成功，删除不成功。我们希望能够形成事务，两者只能同时发生。

```java
public class UserMapperImpl implements UserMapper {
    private SqlSessionTemplate sqlSessionTemplate;

    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public int adduser(User user) {
        return sqlSessionTemplate.getMapper(UserMapper.class).adduser(user);
    }

    @Override
    public int deleteuser(int id) {
        return sqlSessionTemplate.getMapper(UserMapper.class).deleteuser(id);
    }

    @Override
    public List<User> selectuser() {
        User user = new User(8,"不该存在的8","66666");
        adduser(user);
        deleteuser(8);
        return sqlSessionTemplate.getMapper(UserMapper.class).selectuser();
    }
}
```

之后向spring容器中注册

#### MyTest

```java
public class MyTest {
    @Test
    public void test(){
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        UserMapper userDao = context.getBean("UserDao", UserMapper.class);
        List<User> selectuser = userDao.selectuser();
        System.out.println(selectuser);
    }
}
```

我们发现事情并非如设想一般，虽然删除失败了，但是插入成功了。

我们希望为两者织入事务

### Spring 事务支持

Spring在不同的事务管理API之上定义了一个抽象层，使得开发人员不必了解底层的事务管理API就可以使用Spring的事务管理机制。Spring支持编程式事务管理和声明式的事务管理。

#### 编程式事务管理（不建议使用）

- 将事务管理代码嵌到业务方法中来控制事务的提交和回滚
- 缺点：必须在每个事务操作业务逻辑中包含额外的事务管理代码

#### 声明式事务管理

- 一般情况下比编程式事务好用。
- 将事务管理代码从业务方法中分离出来，以声明的方式来实现事务管理。
- 将事务管理作为横切关注点，通过aop方法模块化。Spring中通过Spring AOP框架支持声明式事务管理。

#### 导入tx约束

```xml
xmlns:tx="http://www.springframework.org/schema/tx"

http://www.springframework.org/schema/tx
http://www.springframework.org/schema/tx/spring-tx.xsd">
```

#### 事务管理器

- 无论使用Spring的哪种事务管理策略（编程式或者声明式）事务管理器都是必须的。
- 就是 Spring的核心事务管理抽象，管理封装了一组独立于技术的方法。
- Spring中提供了 **DataSourceTransactionManager**作为事务管理器

### 声明式事务实例

#### 配置事务管理器

```xml
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <constructor-arg ref="DataSource"></constructor-arg>
    </bean>
```

#### 配置事务传播

**spring事务传播特性：**

事务传播行为就是多个事务方法相互调用时，事务如何在这些方法间传播。spring支持7种事务传播行为：

- **propagation_requierd**：如果当前没有事务，就新建一个事务，如果已存在一个事务中，加入到这个事务中，这是最常见的选择。
- **propagation_supports**：支持当前事务，如果没有当前事务，就以非事务方法执行。
- **propagation_mandatory**：使用当前事务，如果没有当前事务，就抛出异常。
- **propagation_required_new**：新建事务，如果当前存在事务，把当前事务挂起。
- **propagation_not_supported**：以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
- **propagation_never**：以非事务方式执行操作，如果当前事务存在则抛出异常。
- **propagation_nested**：如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则执行与propagation_required类似的操作

Spring 默认的事务传播行为是 PROPAGATION_REQUIRED，它适合于绝大多数的情况。

```xml
    <tx:advice id = "transactionmanager" transaction-manager="transactionManager">
        <tx:attributes>
            <tx:method name="selectuser" propagation="REQUIRED"/>
        </tx:attributes>
    </tx:advice>
```

tx:method 配置的名字即为需要事务增强的方法名

#### 配置AOP切面

```xml
    <aop:config>
        <aop:pointcut id="transactionpoint" expression="execution(* cn.rainingapple.dao.UserMapper.*(..))"/>
        <aop:advisor advice-ref="transactionmanager" pointcut-ref="transactionpoint"></aop:advisor>
    </aop:config>
```

运行测试，可以发现二者组成了事务。





