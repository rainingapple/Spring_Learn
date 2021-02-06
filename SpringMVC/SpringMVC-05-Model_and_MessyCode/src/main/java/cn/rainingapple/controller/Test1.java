package cn.rainingapple.controller;


import cn.rainingapple.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Test1 {

    @RequestMapping("/test1")
    public String test1(String name){
        System.out.println(name);
        return "test";
    }

    @RequestMapping("/test2")
    public String test2(@RequestParam("username") String username){
        System.out.println(username);
        return "test";
    }

    @RequestMapping("/test3")
    public String test3(User user){
        System.out.println(user);
        return "test";
    }

}
