# Spring-05-Autowired

在Spring中经常遇见繁杂的属性配置工作，很多value以及ref值需要手动配置。

考虑到我们的类的名称和类型可以共同遵循某些**规范**，我们就可以依据这些进行自动装配。

由此就引出了 **基于名称自动装配ByName** 和 **基于类型的自动装配ByType**

我们可以**基于配置文件实现自动装配**，或者**基于注解实现自动装配**



Spring的自动装配需要从两个角度来实现，或者说是两个操作：

1. **组件扫描(component scanning)**：spring会自动发现应用上下文中所创建的bean；
2. **自动装配(autowiring)**：spring自动满足bean之间的依赖，也就是我们说的IoC/DI；

组件扫描和自动装配组合发挥巨大威力，使得显示的配置降低到最少。

**推荐不使用自动装配xml配置 , 而使用注解 .**



**参考**

Spring Core官方文档

https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/core.html#beans-annotation-config

狂神说博客

https://www.cnblogs.com/renxuw/p/12994080.html

https://mp.weixin.qq.com/s/kvp_3Uva1J2Q5ZVqCUzEsA

**Github仓库**

https://github.com/rainingapple/Spring_Learn

<!--more-->

## 原始手动装配

### 用例

#### Cat

```java
public class Cat {
    public void say(){
        System.out.println("miao!");
    }
}
```

#### Dog

```java
public class Dog {
    public void say(){
        System.out.println("wang!");
    }
}

```

#### Person

```java
public class Person {

    private Cat cat;
    private Dog dog;
    private String name;

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "cat=" + cat +
                ", dog=" + dog +
                ", name='" + name + '\'' +
                '}';
    }
}
```

### 手动装配xml

```xml

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="cat" class="cn.rainingapple.Pojo.XML_based.Cat"></bean>
    <bean id="dog" class="cn.rainingapple.Pojo.XML_based.Dog"></bean>
    <bean id="person" class="cn.rainingapple.Pojo.XML_based.Person">
        <property name="name" value="张三"></property>
        <property name="cat" ref="cat"></property>
        <property name="dog" ref="dog"></property>
    </bean>
</beans>
```

### MyTest

```java
public class MyTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        Person person = context.getBean("person", Person.class);
        person.getCat().say();
        person.getDog().say();
    }
}
```

## 基于配置文件的自动装配

### 基于名称ByName

```xml
<!--使用byName自动装配，需要注意的是必须完全一致，大小写敏感-->
<bean id="person" class="cn.rainingapple.Pojo.XML_based.Person" autowire="byName">
    <property name="name" value="张三"></property>
</bean>
```

考虑到大小写敏感，在进行bean的id命名的时候一般同一像变量名一样用小写

### 基于类型ByType

```xml
<!--使用byType自动装配，需要注意同一类型必须只有一个-->
<bean id="person" class="cn.rainingapple.Pojo.XML_based.Person" autowire="byType">
    <property name="name" value="张三"></property>
</bean>
```

如果使用ByType，那么同一类型只能有一个

## 基于注解的自动装配

### 导入相应约束

#### 引入文件头

```xml
xmlns:context="http://www.springframework.org/schema/context"

http://www.springframework.org/schema/context
http://www.springframework.org/schema/context/spring-context.xsd
```

#### 开启属性注解支持

```xml
<context:annotation-config/>
```

#### 引入注解后的模板

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">

    <context:annotation-config/>

</beans>
```

### @Autowired

通过@Autowire实现ByType装配

#### 加入@Autowired的Person

```java
public class Person {

    @Autowired
    private Cat cat;
    @Autowired
    private Dog dog;
    private String name;

    public Cat getCat() {
        return cat;
    }

    public Dog getDog() {
        return dog;
    }

    @Override
    public String toString() {
        return "Person{" +
                "cat=" + cat +
                ", dog=" + dog +
                ", name='" + name + '\'' +
                '}';
    }
}
```

#### 新的xml配置文件

```xml
<bean id="cat" class="cn.rainingapple.Pojo.Annotation_based.Cat"></bean>
<bean id="dog" class="cn.rainingapple.Pojo.Annotation_based.Dog"></bean>
<bean id="person" class="cn.rainingapple.Pojo.Annotation_based.Person"></bean>
```

### @Qualifier

#### 加入@Qualifier的Person

只能配合Autowired使用，可以指定名称实现按名字匹配

```java
public class Person {

    @Autowired
    @Qualifier(value = "cat2")
    private Cat cat;
    @Autowired
    @Qualifier(value = "dog1")
    private Dog dog;
    private String name;

    public Cat getCat() {
        return cat;
    }

    public Dog getDog() {
        return dog;
    }

    @Override
    public String toString() {
        return "Person{" +
                "cat=" + cat +
                ", dog=" + dog +
                ", name='" + name + '\'' +
                '}';
    }
}
```

#### 新的xml配置文件

```xml
<bean id="dog1" class="cn.rainingapple.Pojo.Annotation_based.Dog"></bean>
<bean id="dog2" class="cn.rainingapple.Pojo.Annotation_based.Dog"></bean>
<bean id="cat1" class="cn.rainingapple.Pojo.Annotation_based.Cat"></bean>
<bean id="cat2" class="cn.rainingapple.Pojo.Annotation_based.Cat"></bean>

<bean id="person" class="cn.rainingapple.Pojo.Annotation_based.Person"></bean>
```

### @Resource

- @Resource如有指定的name属性，先按该属性进行byName方式查找装配；
- 其次再进行默认的byName方式进行装配；
- 如果以上都不成功，则按byType的方式自动装配。
- 都不成功，则报异常。

#### 加入@Resource的Person

```java
public class Person {

    @Resource(name = "cat1")
    private Cat cat;
    @Resource
    private Dog dog;
    private String name;

    public Cat getCat() {
        return cat;
    }

    public Dog getDog() {
        return dog;
    }

    @Override
    public String toString() {
        return "Person{" +
                "cat=" + cat +
                ", dog=" + dog +
                ", name='" + name + '\'' +
                '}';
    }
}
```

#### 新的xml配置文件

```xml
<bean id="dog1" class="cn.rainingapple.Pojo.Annotation_based.Dog"></bean>
<bean id="cat1" class="cn.rainingapple.Pojo.Annotation_based.Cat"></bean>
<bean id="cat2" class="cn.rainingapple.Pojo.Annotation_based.Cat"></bean>

<bean id="person" class="cn.rainingapple.Pojo.Annotation_based.Person"></bean>
```

### @Autowired与@Resource异同

- @Autowired与@Resource都可以用来装配bean。都可以写在字段上，或写在setter方法上。
- @Autowired默认按类型装配（属于spring规范），默认情况下必须要求依赖对象必须存在，如果要允许null 值，可以设置它的required属性为false，如：@Autowired(required=false) ，如果我们想使用名称装配可以结合@Qualifier注解进行
- @Resource默认按照名称进行装配，名称可以通过name属性进行指定。如果没有指定name属性，当注解写在字段上时，默认取字段名进行按照名称查找，如果注解写在setter方法上默认取属性名进行装配。当找不到与名称匹配的bean时才按照类型进行装配。但是需要注意的是，如果name属性一旦指定，就只会按照名称进行装配。
- 它们的作用相同都是用注解方式注入对象，但执行顺序不同。@Autowired先byType，@Resource先byName。

