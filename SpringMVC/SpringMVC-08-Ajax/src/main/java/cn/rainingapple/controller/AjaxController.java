package cn.rainingapple.controller;


import cn.rainingapple.pojo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AjaxController {

    @RequestMapping("/ajax1")
    public void test(String name, HttpServletResponse response) throws IOException {
        if("zndx".equals(name)){
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print("你是中南人");
        }
        else{
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print("你不是中南人");
        }
    }

    @RequestMapping("/ajax2")
    public List<User> test2() throws IOException {
        List<User> userList = new ArrayList<User>();
        userList.add(new User("张三",99,"人妖"));
        userList.add(new User("李四",89,"人妖"));
        userList.add(new User("王五",79,"人妖"));
        return userList;
    }

    @RequestMapping("/ajax3")
    public String ajax3(String name,String pwd){
        String msg = "";
        //模拟数据库中存在数据
        if (name!=null){
            if ("admin".equals(name)){
                msg = "OK";
            }else {
                msg = "用户名输入错误";
            }
        }
        if (pwd!=null){
            if ("123456".equals(pwd)){
                msg = "OK";
            }else {
                msg = "密码输入有误";
            }
        }
        System.out.println(msg);
        return msg; //由于@RestController注解，将msg转成json格式返回
    }
}
