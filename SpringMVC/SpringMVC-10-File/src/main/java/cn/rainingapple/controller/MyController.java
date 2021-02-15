package cn.rainingapple.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@RestController
public class MyController {

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

    @RequestMapping("/download")
    public String downloads(HttpServletResponse response , HttpServletRequest request) throws IOException {
        String  path = request.getServletContext().getRealPath("/upload2");
        String  fileName = "测试.bmp";

        //1、设置response 响应头
        response.reset(); //设置页面不缓存,清空buffer
        response.setCharacterEncoding("UTF-8"); //字符编码
        response.setContentType("multipart/form-data"); //二进制传输数据
        //设置响应头
        response.setHeader("Content-Disposition",
                "attachment;fileName="+ URLEncoder.encode(fileName, "UTF-8"));

        File file = new File(path,fileName);
        //2、 读取文件--输入流
        InputStream input=new FileInputStream(file);
        //3、 写出文件--输出流
        OutputStream out = response.getOutputStream();

        byte[] buff =new byte[1024];
        int len=0;
        //4、执行 写出操作
        while((len= input.read(buff))!= -1){
            out.write(buff, 0, len);
            out.flush();
        }
        out.close();
        input.close();
        return null;
    }
}
