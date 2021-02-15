# SpringMVC-10-File

- 文件上传是项目开发中最常见的功能之一 ,springMVC 可以很好的支持文件上传
- SpringMVC上下文中默认没有装配MultipartResolver，默认情况下其不能处理文件上传工作。
- 如果想使用Spring的文件上传功能，则需要在上下文中配置MultipartResolver。
- 为了能上传文件，必须将表单的method设置为POST，并将enctype设置为multipart/form-data。

<!--more-->

**enctype 属性**

- application/x-www=form-urlencoded：默认方式，只处理表单域中的 value 属性值，采用这种编码方式的表单会将表单域中的值处理成 URL 编码方式。
- multipart/form-data：这种编码方式会以二进制流的方式来处理表单数据，这种编码方式会把文件域指定文件的内容也封装到请求参数中，不会对字符编码。
- text/plain：除了把空格转换为 "+" 号外，其他字符都不做编码处理，这种方式适用直接通过表单发送邮件。

```xml
<form action="" enctype="multipart/form-data" method="post">
   <input type="file" name="file"/>
   <input type="submit">
</form>
```

**Commons FileUpload**

- Servlet3.0规范已经提供方法来处理文件上传，但这种上传需要在Servlet中完成。
- 而Spring MVC则提供了更简单的封装。
- Spring MVC为文件上传提供了直接的支持，这种支持是用即插即用的MultipartResolver实现的。
- Spring MVC使用Apache Commons FileUpload技术实现了一个MultipartResolver实现类：
- CommonsMultipartResolver。因此，SpringMVC的文件上传还需要依赖Apache Commons FileUpload的组件。

**参考**

spring-webmvc官方文档

https://docs.spring.io/spring-framework/docs/5.2.12.RELEASE/spring-framework-reference/web.html#spring-web

狂神说博客

https://blog.csdn.net/qq_33369905/article/details/105828924

**Github仓库**

https://github.com/rainingapple/Spring_Learn

## 导入依赖

```xml
<!--文件上传-->
<dependency>
   <groupId>commons-fileupload</groupId>
   <artifactId>commons-fileupload</artifactId>
   <version>1.3.3</version>
</dependency>
<!--servlet-api导入高版本的-->
<dependency>
   <groupId>javax.servlet</groupId>
   <artifactId>javax.servlet-api</artifactId>
   <version>4.0.1</version>
</dependency>
```

## 配置multipartResolver

```xml
<bean id="multipartResolver"  class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
   <!-- 请求的编码格式，必须和jSP的pageEncoding属性一致，以便正确读取表单的内容，默认为ISO-8859-1 -->
   <property name="defaultEncoding" value="utf-8"/>
</bean>
```

## 上传文件

### 基础版

```java
    @RequestMapping("/upload1")
    public String fileUpload1(@RequestParam("file")CommonsMultipartFile file,HttpServletRequest request) throws IOException {
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        if(originalFilename.isEmpty()){
            return "redirect:/index.jsp";
        }
        System.out.println(originalFilename);

        //获得父路径的File
        String path = request.getServletContext().getRealPath("/upload1");
        File file1 = new File(path);
        if(!file1.exists()){
            file1.mkdir();
        }
        System.out.println(file1);

        //获得输入输出流进行写入
        InputStream inputStream = file.getInputStream();
        FileOutputStream outputStream = new FileOutputStream(new File(file1, originalFilename));
        int len = 0;
        byte[] buffer = new byte[1024];
        while ((len=inputStream.read(buffer))!=-1){
            outputStream.write(buffer,0,len);
            outputStream.flush();
        }
        outputStream.close();
        inputStream.close();
        return "redirect:/index.jsp";
    }
```

### 精简版（推荐）

直接使用file.transferTo完成上传，省略了输入输出流

```java
   @RequestMapping("/upload2")
    public String fileUpload2(@RequestParam("file") CommonsMultipartFile file , HttpServletRequest request) throws IOException {
        //上传路径保存设置
        String path = request.getServletContext().getRealPath("/upload2");
        File realPath = new File(path);
        if (!realPath.exists()){
            realPath.mkdir();
        }
        //上传文件地址
        System.out.println("上传文件保存地址："+realPath);

        //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
        file.transferTo(new File(realPath +"/"+ file.getOriginalFilename()));

        return "redirect:/index.jsp";
    }
```

## 下载文件

了解即可，狂神这个太复杂了。很多操作可以省略

前端一个标签即可实现下载\<a href="/download">点击下载\</a>

```java
    @RequestMapping("/download")
    public String downloads(HttpServletResponse response , HttpServletRequest request) throws IOException {
        String  path = request.getServletContext().getRealPath("/upload2");
        String  fileName = "测试.bmp";

        //设置response
        response.reset(); //设置页面不缓存,清空buffer
        response.setCharacterEncoding("UTF-8"); //字符编码
        response.setContentType("multipart/form-data"); //二进制传输数据
        response.setHeader("Content-Disposition",
                "attachment;fileName="+ URLEncoder.encode(fileName, "UTF-8"));

        //获得输入输出流进行写入
        File file = new File(path,fileName);
        InputStream input=new FileInputStream(file);
        OutputStream out = response.getOutputStream();

        byte[] buff =new byte[1024];
        int len=0;
        while((len= input.read(buff))!= -1){
            out.write(buff, 0, len);
            out.flush();
        }
        out.close();
        input.close();
        return null;
    }
```

## 上传表单

```jsp
<form action="/upload" enctype="multipart/form-data" method="post">
 <input type="file" name="file"/>
 <input type="submit" value="upload">
</form>
```

后续在SpringBoot中对下载有更好的支持