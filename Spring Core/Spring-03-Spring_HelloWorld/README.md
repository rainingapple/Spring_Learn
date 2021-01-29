# Spring-03-Spring_HelloWorld

## Spring元数据组织方式

- **Annotation-based** configuration: Spring 2.5 introduced support for annotation-based configuration metadata.
- **Java-based** configuration: Starting with Spring 3.0, many features provided by the Spring JavaConfig project became part of the core Spring Framework. Thus, you can define beans external to your application classes by using Java rather than XML files. 

**参考**

Spring Core官方文档

https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/core.html#beans-factory-metadata

狂神说博客

https://www.cnblogs.com/renxuw/p/12994080.html

https://mp.weixin.qq.com/s/Sa39ulmHpNFJ9u48rwCG7A

**Github仓库**

https://github.com/rainingapple/Spring_Learn

## 基于注解的Spring实例

### 编写一个Hello POJO类

```java
public class Hello {
   private String name;

   public String getName() {
       return name;
  }
   public void setName(String name) {
       this.name = name;
  }

   public void show(){
       System.out.println("Hello,"+ name );
  }
}
```

### 编写我们的spring 配置文件 ,

这里我们命名为beans.xml，名字可以任意取，但其实这样并不符合命名规范

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <!--通过bean创建对象-->
    <bean id="Hello" class="cn.rainingapple.pojo.Hello">
        <property name="str" value="123456"></property>
    </bean>

</beans>
```

### MyTest

```java
@Test
public void test(){
   //解析beans.xml文件 , 生成管理相应的Bean对象
   ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
   //getBean : 参数即为spring配置文件中bean的id .
   Hello hello = (Hello) context.getBean("hello");
   hello.show();
}
```

### 从Spring来看IOC

上一节中介绍了控制反转的思想，通过引入set方法实现Dao层的灵活调用。

而在Spring中，我们将对象托管到Spring中，由Spring生成我们仅从中取用。

程序本身不创建对象，只是接收对象。

通过更改配置文件灵活地更改选择的对象，进而实现控制反转

## Spring重写上节程序

### 配置文件beans_DAO.xml

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="MySql" class="cn.rainingapple.pojo.Dao.userDaoImplMysql"></bean>
    <bean id="Oracle" class="cn.rainingapple.pojo.Dao.userDaoImplOracle"></bean>
    <bean id="SqlServer" class="cn.rainingapple.pojo.Dao.userDaoImplSqlServer"></bean>
    
    <!--这里可以通过更改ref的值灵活变更配置，进而实现IOC-->
    <bean id="service" class="cn.rainingapple.pojo.Service.UserServiceImpl">
        <property name="userDao" ref="Oracle"></property>
    </bean>
</beans>
```

### MyTest_DAO

```java
public class MyTest_Dao {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans_dao.xml");
        UserServiceImpl service = (UserServiceImpl) context.getBean("service");
        service.getname();
    }
}
```

## Spring构造方式

### 无参构造

#### 不需要特殊指明额外的参数

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="Apple" class="cn.rainingapple.DI_Test.Apple_No_Arg">
        <!--无参构造不需要constructer-arg参数，默认就会调用-->
        <property name="name" value="233"></property>
    </bean>

</beans>
```

### 有参构造

#### 有参构造有三种方式，通过索引、名称、类型。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <!--<bean id="Apple" class="cn.rainingapple.DI_Test.Apple_Arg">
        <constructor-arg index="0" value="通过索引进行构造"></constructor-arg>
    </bean>-->

    <!--<bean id="Apple" class="cn.rainingapple.DI_Test.Apple_Arg">
        <constructor-arg name="apple" value="通过名称进行构造"></constructor-arg>
    </bean>-->

    <bean id="Apple" class="cn.rainingapple.DI_Test.Apple_Arg">
        <constructor-arg type="java.lang.String" value="通过类型进行构造"></constructor-arg>
    </bean>

</beans>
```

#### 在使用索引构造时，可以不明确指明第几位

假设这个实体类需要两个参数来构造

```xml
<bean id="Apple" class="cn.rainingapple.DI_Test.Apple_Arg">
        <constructor-arg value="通过索引进行构造"></constructor-arg>
        <constructor-arg value="12"></constructor-arg>
    </bean>
```

## Spring 配置

### alias 别名

```xml
<!--取别名-->
<alias name="Apple" alias="Green_Apple"></alias>
```

### beans配置

#### xml配置文件

```xml
<bean id="Apple" name="u1 u2,u3;u4" class="cn.rainingapple.DI_Test.Apple_Arg">
        <constructor-arg value="通过索引进行构造"></constructor-arg>
    </bean>
```

#### 测试类

```java
public class MyTest_Configure_test {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans_configure_test.xml");
        //所有的别名都是一样的效果
        //Apple_Arg apple = (Apple_Arg) context.getBean("Apple");
        //Apple_Arg apple = (Apple_Arg) context.getBean("Green_Apple");
        //Apple_Arg apple = (Apple_Arg) context.getBean("u1");
        //Apple_Arg apple = (Apple_Arg) context.getBean("u2");
        //Apple_Arg apple = (Apple_Arg) context.getBean("u3");
        Apple_Arg apple = (Apple_Arg) context.getBean("u4");
        apple.show();
    }
}
```

### import

一般用于团队开发，可以把多个配置文件导入合并为一个

```xml
<import resource="beans_configure_test.xml"></import>
```

这里仅了解用法，具体细节之后随着实践再逐渐了解