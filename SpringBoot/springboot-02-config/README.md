# SpringBoot-02-Config

补充一些Boot后常用的配置：yaml、JSR303校验、多环境配置

## yaml

**YAML**是一个可读性高，用来表达数据[序列化](https://baike.baidu.com/item/序列化)的格式。YAML参考了其他多种语言，包括：[C语言](https://baike.baidu.com/item/C语言)、[Python](https://baike.baidu.com/item/Python)、[Perl](https://baike.baidu.com/item/Perl)，并从[XML](https://baike.baidu.com/item/XML)、电子邮件的数据格式（RFC 2822）中获得灵感。Clark Evans在2001年首次发表了这种语言.

YAML是"YAML Ain't a Markup Language"（YAML不是一种[标记语言](https://baike.baidu.com/item/标记语言)）的[递归缩写](https://baike.baidu.com/item/递归缩写)。在开发的这种语言时，*YAML* 的意思其实是："Yet Another Markup Language"（仍是一种[标记语言](https://baike.baidu.com/item/标记语言)），但为了强调这种语言以数据做为中心，而不是以标记语言为重点，而用反向缩略语重命名。

<!--more-->

**yaml 和 xml**

传统xml配置：

```xml
<server>    
    <port>8081<port>
</server>
```

yaml配置：

```yaml
server：
  port: 8080
```

### yaml基础语法

#### 字面量

数字，布尔值，字符串等字面量直接写在后面就可以，字符串默认不用加上双引号或者单引号；

```
k: v
```

注意：

- “ ” 双引号，不会转义字符串里面的特殊字符 ， 特殊字符会作为本身想表示的意思；

  比如 ：name: "kuang \n shen"  输出 ：kuang  换行  shen

- '' 单引号，会转义特殊字符 ， 特殊字符最终会变成和普通字符一样输出

  比如 ：name: ‘kuang \n shen’  输出 ：kuang  \n  shen

#### 对象、键值对

```yml
key:
  key1: sss
  key2: ddd
```

在下一行来写对象的属性和值得关系，注意缩进；比如：

```yml
student:
    name: qinjiang
    age: 3
```

行内写法

```yml
student: {name: qinjiang,age: 3}
```

#### 数组与集合

用 - 值表示数组中的一个元素,比如：

```yml
pets:
 - cat
 - dog
 - pig
```

行内写法

```yml
pets: [cat,dog,pig]
```

### yaml为实体类赋值

#### pojo

##### Dog

```java
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dog {
    private String name;
    private Integer age;
}
```

##### Person

```java
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "person")
public class Person {
    private String name;
    private Integer age;
    private Boolean happy;
    private Date birth;
    private Map<String,Object> maps;
    private List<Object> lists;
    private Dog dog;
}
```

ConfigurationProperties(prefix = "person") 表明注解对应的yaml配置项是person

**对应需要引入依赖**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```

#### application.yaml

```yaml
person:
  name: 张三
  age: 13
  happy: true
  birth: 2020/12/31
  maps: {1: 翻转,2: 起飞}
  lists:
    - 999
    - 啦啦啦
    - 啦啦啦啦
  dog:
    name: 啦啦啦啦啦
    age: 65536
```

#### Test

```java
@SpringBootTest
class Springboot02ConfigApplicationTests {

    @Autowired
    Person person;

    @Test
    void contextLoads() {
        System.out.println(person);
    }
}
```

## JSR303数据校验

我们经常在前端进行一些格式方面的校验，当然我们在后端也可以做，Spring框架提供了注解版的支持

### 导入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

### @Validated

```java
@Validated //注释代表使该实体类支持JSR303校验
public class Person {
    @Email //必须符合email格式
    private String name;
    @Min(value = 12) 
    private Integer age;
    @NotNull(message = "幸福情况不能为空")
    private Boolean happy;
    private Date birth;
    private Map<String,Object> maps;
    private List<Object> lists;
    private Dog dog;
}
```

提供了多种多样的检验，支持正则表达式。同时报错的提示非常人性化

类型大致如下

```
空检查
@Null       验证对象是否为null
@NotNull    验证对象是否不为null, 无法查检长度为0的字符串
@NotBlank   检查约束字符串是不是Null还有被Trim的长度是否大于0,只对字符串,且会去掉前后空格.
@NotEmpty   检查约束元素是否为NULL或者是EMPTY.
    
Booelan检查
@AssertTrue     验证 Boolean 对象是否为 true  
@AssertFalse    验证 Boolean 对象是否为 false  
    
长度检查
@Size(min=, max=) 验证对象（Array,Collection,Map,String）长度是否在给定的范围之内  
@Length(min=, max=) string is between min and max included.

日期检查
@Past       验证 Date 和 Calendar 对象是否在当前时间之前  
@Future     验证 Date 和 Calendar 对象是否在当前时间之后  
@Pattern    验证 String 对象是否符合正则表达式的规则

.......等等
除此以外，我们还可以自定义一些数据校验规则
```

## 多环境配置

在工程开发中我们可能同时配置多个环境：默认环境、开发环境、测试环境

profile是Spring对不同环境提供不同配置功能的支持，可以通过激活不同的环境版本，实现快速切换环境；

### 配置文件切换

我们在主配置文件编写的时候，文件名可以是 application-{profile}.properties/yml , 用来指定多个环境版本；

**例如：**

application-test.properties 代表测试环境配置

application-dev.properties 代表开发环境配置

但是Springboot并不会直接启动这些配置文件，它**默认使用application.properties主配置文件**；

我们需要通过一个配置来选择需要激活的环境：

**properties：**

```properties
spring.profiles.active=dev
```

**yaml**

```yaml
spring:
  profiles:
    active: dev
```

### yaml的多文档块

yaml可以在一个文档中方便配置多个环境

和properties配置文件中一样，但是使用yml去实现不需要创建多个配置文件，更加方便了 !

```yaml
server:
  port: 8081

spring:
  profiles:
    active: dev

---
server:
  port: 8082
spring:
  config:
    activate:
      on-profile: dev

---
server:
  port: 8083
spring:
  config:
    activate:
      on-profile: test
```

**注意：如果yml和properties同时都配置了端口，并且没有激活其他环境 ， 默认会使用properties配置文件的！**

### 配置文件位置

**外部加载配置文件的方式十分多，我们选择最常用的即可，在开发的资源文件中进行配置！**

[官方外部配置文件说明参考文档](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config-optional-prefix)

#### 优先级

springboot 启动会扫描以下位置的application.properties或者application.yml文件作为Spring boot的默认配置文件：

```
优先级1：项目路径下的config文件夹配置文件
优先级2：项目路径下配置文件
优先级3：资源路径下的config文件夹配置文件
优先级4：资源路径下配置文件
```

优先级由高到底，高优先级的配置会覆盖低优先级的配置；

**SpringBoot会从这四个位置全部加载主配置文件；互补配置；**

## 自动配置原理

详细的可以参考狂神的博客：[运行原理初探](https://mp.weixin.qq.com/s?__biz=Mzg2NTAzMTExNg%3D%3D&chksm=ce6107fcf9168eeaa5381228dad0e888ffc03401bc51e4bc7637bd46604b8e6e468cc8b43956&idx=1&mid=2247483743&scene=21&sn=431a5acfb0e5d6898d59c6a4cb6389e7#wechat_redirect) 、[自动配置原理初探](https://mp.weixin.qq.com/s?__biz=Mzg2NTAzMTExNg%3D%3D&chksm=ce6107d5f9168ec34f59d88c5a7cfa592ab2c1a5bf02cc3ed7bbb7b4f4e93d457144a6843a23&idx=1&mid=2247483766&scene=21&sn=27739c5103547320c505d28bec0a9517#wechat_redirect) 

简单的来说，自动配置的核心是...AutoConfiguration和...Properties

我们可以在项目的依赖中找到AutoConfiguration

![屏幕截图 2021-02-17 152520](https://picgo-mk.oss-cn-beijing.aliyuncs.com/20210217152833.png)

spring.factories中说明了我们需要加载的自动配置类

![屏幕截图 2021-02-17 152532](https://picgo-mk.oss-cn-beijing.aliyuncs.com/20210217152906.png)

其中的众多的自动配置类会对应绑定自己的Properties文件

![屏幕截图 2021-02-17 152603](https://picgo-mk.oss-cn-beijing.aliyuncs.com/20210217152946.png)

对应的Properties会声明自己在配置中对应的前缀

![屏幕截图 2021-02-17 152618](https://picgo-mk.oss-cn-beijing.aliyuncs.com/20210217153011.png)

## 参考

springboot官方文档

https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config

狂神说博客

https://blog.csdn.net/qq_33369905/article/details/105828924

## Github仓库

https://github.com/rainingapple/Spring_Learn