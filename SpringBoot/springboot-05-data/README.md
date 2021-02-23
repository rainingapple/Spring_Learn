# SpringBoot-05-Data

## 整合JDBC

新建SpringBoot项目，对应勾选“JDBC API”，“MySQL Driver”

### 相关依赖

新建时勾选对应项目后会对应添加 spring-boot-starter-jdbc 与 mysql-connector-java 

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 配置datasource

```yml
spring:
  datasource:
    username: root
    password: *****
    url: jdbc:mysql://你的url:3306/demo01?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8
    driver-class-name: com.mysql.cj.jdbc.Driver
```

对应使用com.mysql.cj.jdbc.Driver，com.mysql.jdbc.Driver已弃用

### 测试

```java
    @Test
    public void contextLoads() throws SQLException {
        //看一下默认数据源
        System.out.println(dataSource.getClass());
        //获得连接
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
        //关闭连接
        connection.close();
    }
```

输出显示默认数据源为 : com.zaxxer.hikari.HikariDataSource

对应的配置我们详见 DataSourceAutoConfiguration

可以在配置文件中通过更改 spring.datasource.type使用全限定名指定数据源

## JDBCTemplate

- 通过数据源得到数据库连接，进而可以使用JDBC 语句操作数据库；
- 即使不使用第三方框架，如 MyBatis等，Spring 本身也对原生的JDBC 做了轻量级的封装，即JdbcTemplate。
- 数据库操作的所有 CRUD 方法都在 JdbcTemplate 中。
- Spring Boot 不仅提供了默认的数据源，同时配置了默认 JdbcTemplate
- JdbcTemplate 自动配置依赖 JdbcTemplateConfiguration 类

### JdbcTemplate主要方法

- **execute**方法：可以用于执行任何SQL语句，一般用于执行DDL语句；
- **update**方法及**batchUpdate**方法：update方法用于执行增删改语句；batchUpdate用于执行批处理；
- **query**方法及**queryForXXX**方法：用于执行查询相关语句；
- **call**方法：用于执行**存储过程**、**函数**相关语句。

### 简单使用

```java
@RestController
@RequestMapping("/jdbc")
public class JdbcController {

    /**
     * Spring Boot 默认提供了数据源，默认提供了 org.springframework.jdbc.core.JdbcTemplate
     * JdbcTemplate 中会自己注入数据源，用于简化 JDBC操作
     * 还能避免一些常见的错误,使用起来也不用再自己来关闭数据库连接
     */
    @Autowired
    JdbcTemplate jdbcTemplate;

    //查询employee表中所有数据
    //List 中的1个 Map 对应数据库的 1行数据
    //Map 中的 key 对应数据库的字段名，value 对应数据库的字段值
    @GetMapping("/list")
    public List<Map<String, Object>> userList(){
        String sql = "select * from employee";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return maps;
    }
    
    //新增一个用户
    @GetMapping("/add")
    public String addUser(){
        //插入语句，注意时间问题
        String sql = "insert into employee(last_name, email,gender,department,birth)" +
                " values ('狂神说','24736743@qq.com',1,101,'"+ new Date().toLocaleString() +"')";
        jdbcTemplate.update(sql);
        //查询
        return "addOk";
    }

    //修改用户信息
    @GetMapping("/update/{id}")
    public String updateUser(@PathVariable("id") int id){
        //插入语句
        String sql = "update employee set last_name=?,email=? where id="+id;
        //数据
        Object[] objects = new Object[2];
        objects[0] = "秦疆";
        objects[1] = "24736743@sina.com";
        jdbcTemplate.update(sql,objects);
        //查询
        return "updateOk";
    }

    //删除用户
    @GetMapping("/delete/{id}")
    public String delUser(@PathVariable("id") int id){
        //插入语句
        String sql = "delete from employee where id=?";
        jdbcTemplate.update(sql,id);
        //查询
        return "deleteOk";
    }
    
}
```

相应的也可以很容易的配置Druid等数据源

## MyBatis-SpringBoot

官网：http://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/

demo为纯注解开发，当然也可以兼容过去的xml配置

### 创建Mapper

对应的Mapper标识会使CityMapper可以被扫描到

```java
@Mapper
public interface CityMapper {

  @Select("SELECT * FROM CITY WHERE state = #{state}")
  City findByState(@Param("state") String state);

}
```

### 使用mybatisMapper

简单注入即可

```java
    @Autowired
    MybatisMapper mybatisMapper;

    @RequestMapping("/list")
    public List<Map<String,Object>> selectuser(){
        List<Map<String, Object>> maps = mybatisMapper.selectuser();
        return maps;
    }
```

### 使用 SqlSession

An instance of a `SqlSessionTemplate` is created and added to the Spring context, so you can use the MyBatis API letting it be injected into your beans like follows(available on Spring 4.3+):

```java
@Component
public class CityDao {

  private final SqlSession sqlSession;

  public CityDao(SqlSession sqlSession) {
    this.sqlSession = sqlSession;
  }

  public City selectCityById(long id) {
    return this.sqlSession.selectOne("selectCityById", id);
  }

}
```