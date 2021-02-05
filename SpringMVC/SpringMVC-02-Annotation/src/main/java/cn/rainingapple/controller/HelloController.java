package cn.rainingapple.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {
    @RequestMapping("/hello")
    public String testcontroller(Model model){
        model.addAttribute("msg","注解版");
        return "hello";
    }
}
