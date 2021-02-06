# SpringMVC-06-json

## JSON

- JSON(JavaScript Object Notation, JS 对象标记) 是一种**轻量级**的数据交换格式，目前使用特别广泛。
- 采用完全独立于编程语言的**文本格式**来存储和表示数据。
- 简洁和清晰的层次结构使得 JSON 成为理想的数据交换语言。
- 易于人阅读和编写，同时也易于机器解析和生成，并有效地**提升网络传输效率**。

在 JavaScript 语言中，一切都是对象，任何JavaScript 支持的类型都可以通过 JSON 来表示

- **对象**表示为**键值对**，数据由逗号分隔
- 花括号保存**对象**
- 方括号保存**数组**

<!--more-->

**参考**

spring-webmvc官方文档

https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/web.html#spring-web

狂神说博客

https://blog.csdn.net/qq_33369905/article/details/105828924

**Github仓库**

https://github.com/rainingapple/Spring_Learn

### JSON 键值对

**JSON 键值对**是用来保存 JavaScript 对象的一种方式，和 JavaScript 对象的写法也大同小异

键值对组合中的键名写在前面并用双引号 "" 包裹，使用冒号 : 分隔，然后紧接着值：

```json
{"name": "啦啦啦"}
{"age": "3"}
{"sex": "男"}
```

JSON 是 JavaScript 对象的字符串表示法，它使用文本表示一个 JS 对象的信息，本质是一个字符串。

```js
var obj = {a: 'Hello', b: 'World'}; //这是一个对象，注意键名也是可以使用引号包裹的
var json = '{"a": "Hello", "b": "World"}'; //这是一个 JSON 字符串，本质是一个字符串
```

### JSON 和 JavaScript 对象互转

要实现从JSON字符串转换为JavaScript 对象，使用 JSON.parse() 方法：

```js
var obj = JSON.parse('{"a": "Hello", "b": "World"}');
//结果是 {a: 'Hello', b: 'World'}
```

要实现从JavaScript 对象转换为JSON字符串，使用 JSON.stringify() 方法：

```js
var json = JSON.stringify({a: 'Hello', b: 'World'});
//结果是 '{"a": "Hello", "b": "World"}'
```

## 使用jackson-databind操作JSON

### 导入依赖

```xml
        <!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.11.3</version>
        </dependency>
```

### objectMapper.writeValueAsString()

@**ResponseBody** : 当前方法以web response body格式返回

@**RestController** : 当前整个controller以web response body格式返回

```java
@Controller
public class TestJson1 {
    @RequestMapping("/json1")
    @ResponseBody
    public String test1() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        User user = new User(1,"拉拉",15);
        return mapper.writeValueAsString(user);
    }
}
```

### 使用produces解决乱码

```java
    @RequestMapping(value = "/json2",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String test2() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        User user = new User(1,"拉拉",15);
        return mapper.writeValueAsString(user);
    }
```

### 使用SpringMVC解决乱码(推荐)

具体展开springmvc-servlet.xml中的\<mvc:annotation-driven\>标签

配置 **StringHttpMessageConverter** 与 **MappingJackson2HttpMessageConverter**

```xml
    <mvc:annotation-driven>
        <mvc:message-converters register-defaults="true">
            <bean class="org.springframework.http.converter.StringHttpMessageConverter">
                <constructor-arg value="UTF-8"/>
            </bean>
            <bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
                <property name="objectMapper">
                    <bean class="org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean">
                        <property name="failOnEmptyBeans" value="false"/>
                    </bean>
                </property>
            </bean>
        </mvc:message-converters>
    </mvc:annotation-driven>
```

### 测试JSON列表格式

```java
    @RequestMapping("/json4")
    public String test2() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        User user1 = new User(1,"拉拉",15);
        User user2 = new User(1,"拉拉",15);
        User user3 = new User(1,"拉拉",15);
        User user4 = new User(1,"拉拉",15);
        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);
        list.add(user3);
        list.add(user4);
        return mapper.writeValueAsString(list);
    }
```

### 输出时间

```java
    @RequestMapping("/json6")
    public String test2() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        //configure如果后面明确指定了格式可以不写，不明确指定为标准时写法，时区可能不一致
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        mapper.setDateFormat(sdf);
        Date date = new Date();
        return mapper.writeValueAsString(date);
    }
```

#### 对应的可以提取为工具类

```java
public class JsonUtils {
    public static String getJson(Object object) throws JsonProcessingException {
        return getJson(object,"yyyy-MM-dd HH:mm:ss");
    }

    public static String getJson(Object object,String dateFormat) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        //自定义日期格式对象
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        //指定日期格式
        mapper.setDateFormat(sdf);
        return mapper.writeValueAsString(object);
    }
}
```

