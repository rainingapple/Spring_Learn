package cn.rainingapple.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Test_MVC_ViewResolver {

    @GetMapping("/MVC/test1")
    public String test1(){
        return "success";
    }

    @GetMapping("/MVC/test2")
    public String test2(){
        return "redirect:/otherdir/success.jsp";
    }
}
