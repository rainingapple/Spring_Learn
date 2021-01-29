# Spring-04-DI

当某个角色需要另一个角色的协助时，在传统的程序设计过程中通常由调用者来创建被调用者的实例。

Spring里，创建被调用者的工作不再由调用者来完成，而在Spring中改为由Spring容器来完成，然后注入调用者

这种方式称为依赖注入，创建工作不再由调用者完成，实现控制反转。

DI存在两个主要变体：[基于构造函数的依赖注入](https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/core.html#beans-constructor-injection)和[基于Setter的依赖注入](https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/core.html#beans-setter-injection)。

**参考**

Spring Core官方文档

https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/core.html#beans-dependencies

狂神说博客

https://www.cnblogs.com/renxuw/p/12994080.html

https://mp.weixin.qq.com/s/Nf-cYENenoZpXqDjv574ig

**Github仓库**

https://github.com/rainingapple/Spring_Learn

## 构造器注入

在Spring-03日志中做了初步解释

## Set注入【重点】

### Address

```java
public class Address {
    String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Address{" +
                "address='" + address + '\'' +
                '}';
    }
}
```

### Student

```java
public class Student {
    private String name;
    private Address address;
    private String[] books;
    private List<String> hobbys;
    private Map<String,String> card;
    private Set<String> games;
    private String wife; //这里是用来测试空指针
    private Properties info;

    // 后面是生成的getter和setter还有toString方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String[] getBooks() {
        return books;
    }

    public void setBooks(String[] books) {
        this.books = books;
    }

    public List<String> getHobbys() {
        return hobbys;
    }

    public void setHobbys(List<String> hobbys) {
        this.hobbys = hobbys;
    }

    public Map<String, String> getCard() {
        return card;
    }

    public void setCard(Map<String, String> card) {
        this.card = card;
    }

    public Set<String> getGames() {
        return games;
    }

    public void setGames(Set<String> games) {
        this.games = games;
    }

    public String getWife() {
        return wife;
    }

    public void setWife(String wife) {
        this.wife = wife;
    }

    public Properties getInfo() {
        return info;
    }

    public void setInfo(Properties info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", address=" + address +
                ", books=" + Arrays.toString(books) +
                ", hobbys=" + hobbys +
                ", card=" + card +
                ", games=" + games +
                ", wife='" + wife + '\'' +
                ", info=" + info +
                '}';
    }
}
```

### Set配置实例

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="Address" class="cn.rainingapple.Address">
        <property name="address" value="中南大学"></property>
    </bean>

    <bean id="Student" class="cn.rainingapple.Student">
        <!--第一种，普通value值注入-->
        <property name="name" value="张三"></property>
        <!--第二种，bean的ref注入-->
        <property name="address" ref="Address"></property>
        <!--第三种，数组的array注入-->
        <property name="books" >
            <array>
                <value>飘</value>
                <value>霍乱时期的爱情</value>
                <value>源氏物语</value>
            </array>
        </property>
        <!--第四种，List的list注入-->
        <property name="hobbys">
            <list>
                <value>唱</value>
                <value>跳</value>
                <value>rap</value>
                <value>练球</value>
            </list>
        </property>
        <!--第五种，Map的map注入-->
        <property name="card">
            <map>
                <entry key="身份证" value="1234654654321"></entry>
                <entry key="银行卡" value="454645646"></entry>
                <entry key="校园卡" value="613248432"></entry>
            </map>
        </property>
        <!--第六种，Set的set注入-->
        <property name="games">
            <set>
                <value>DOTA</value>
                <value>炉石传说</value>
                <value>打地鼠</value>
            </set>
        </property>
        <!--第七种，NUll的null注入，注意NULL不是空串-->
        <property name="wife">
            <null></null>
        </property>
        <!--第七种，Properties的props注入-->
        <property name="info">
            <props>
                <prop key="user">root</prop>
                <prop key="password">123456</prop>
            </props>
        </property>
    </bean>

</beans>
```

### MyTest

```java
public class MyTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        Student student = (Student) context.getBean("Student");
        System.out.println(student.toString());

        /* 输出为
        Student{
        name='张三',
        address=Address{address='中南大学'},
        books=[飘, 霍乱时期的爱情, 源氏物语],
        hobbys=[唱, 跳, rap, 练球],
        card={身份证=1234654654321, 银行卡=454645646, 校园卡=613248432},
        games=[DOTA, 炉石传说, 打地鼠],
        wife='null',
        info={user=root, password=123456}
        }*/
    }
}
```

## 拓展方式注入

### p注入与c注入

#### 引入命名空间

要引入p与c需要在xml文件中对应加入命名空间的url

##### p命名空间

```xml
xmlns:p="http://www.springframework.org/schema/p"
```

##### c命名空间

```
xmlns:c="http://www.springframework.org/schema/c"
```

##### 新的xml模板

当引入p注入与c注入之后，对应的xml模板为

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:c="http://www.springframework.org/schema/c"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">
</beans>
```

#### p注入（对应property）

引入命名空间后，对应根据提示输入即可

```xml
<bean id="User" class="cn.rainingapple.User" p:name="张三" p:age="18"></bean>
```

#### c注入（对应constructor）

```xml
<bean id="User" class="cn.rainingapple.User" c:name="张三" c:age="18"></bean>
```

#### MyTest_pc

```java
public class MyTest_pc {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans_pc.xml");
        User user = context.getBean("User", User.class);
        System.out.println(user.toString());
    }
}
```

## Bean 作用域

官网对各个标签的解释如下

| Scope                                                        | Description                                                  |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| [singleton](https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-singleton) | (Default) Scopes a single bean definition to a single object instance for each Spring IoC container. |
| [prototype](https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-prototype) | Scopes a single bean definition to any number of object instances. |
| [request](https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-request) | Scopes a single bean definition to the lifecycle of a single HTTP request. That is, each HTTP request has its own instance of a bean created off the back of a single bean definition. Only valid in the context of a web-aware Spring `ApplicationContext`. |
| [session](https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-session) | Scopes a single bean definition to the lifecycle of an HTTP `Session`. Only valid in the context of a web-aware Spring `ApplicationContext`. |
| [application](https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-application) | Scopes a single bean definition to the lifecycle of a `ServletContext`. Only valid in the context of a web-aware Spring `ApplicationContext`. |
| [websocket](https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/web.html#websocket-stomp-websocket-scope) | Scopes a single bean definition to the lifecycle of a `WebSocket`. Only valid in the context of a web-aware Spring `ApplicationContext`. |

后几个暂时只做了解，后面再详述

### Singleton

- 当一个bean的作用域为Singleton，那么Spring IoC容器中只会存在一个共享的bean实例
- Singleton是单例类型，就是在创建起容器时就同时自动创建了一个bean的对象，不管你是否使用，他都存在了
- 注意，Singleton作用域是Spring中的缺省作用域。

要在XML中将bean定义成singleton，可以这样配置：

```xml
<!--默认-->
<bean id="User" class="cn.rainingapple.User"></bean>
<!--明确指明-->
<bean id="User" class="cn.rainingapple.User" scope="singleton"></bean>
```

### Prototype

- 当一个bean的作用域为Prototype，表示一个bean定义对应多个对象实例。
- Prototype作用域的bean会导致在每次对该bean请求时都会创建一个新的bean实例。
- Prototype是原型类型，它在我们创建容器的时候并没有实例化，而是当我们获取bean的时候才会去创建一个对象，
- 对有状态的bean应该使用prototype作用域，而对无状态的bean则应该使用singleton作用域。

在XML中将bean定义成prototype，可以这样配置：

```xml
<bean id="User" class="cn.rainingapple.User" scope="prototype"></bean>
```

### Request

- 当一个bean的作用域为Request，表示在一次HTTP请求中，一个bean定义对应一个实例
- 即每个HTTP请求都会有各自的bean实例，它们依据某个bean定义创建而成。
- 该作用域仅在基于web的Spring ApplicationContext情形下有效。

使用下面bean定义：

```xml
<bean id="loginAction" class=cn.csdn.LoginAction" scope="request"/>
```

- 针对每次HTTP请求，Spring容器会根据loginAction bean的定义创建一个全新的LoginAction bean实例，
- 该loginAction bean实例仅在当前HTTP request内有效，因此可以根据需要放心的更改所建实例的内部状态，
- 其他请求中根据loginAction bean定义创建的实例，将不会看到这些特定于某个请求的状态变化。
- 当处理请求结束，request作用域的bean实例将被销毁。

### Session

- 当一个bean的作用域为Session，表示在一个HTTP Session中，一个bean定义对应一个实例。
- 该作用域仅在基于web的Spring ApplicationContext情形下有效。

使用下面bean定义：

```xml
<bean id="userPreferences" class="com.foo.UserPreferences" scope="session"/>
```

- 针对某个HTTP Session，Spring容器会根据userPreferences bean定义创建一个全新的userPreferences bean实例，
- 该userPreferences bean仅在当前HTTP Session内有效，可以根据需要放心的更改所建实例的内部状态。
- 其他HTTP Session中根据userPreferences创建的实例，将不会看到这些特定于某个HTTP Session的状态变化。
- 当HTTP Session最终被废弃的时候，在该HTTP Session作用域内的bean也会被废弃掉。