package cn.rainingapple.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Test3 {

    @RequestMapping("/test5")
    public String test1(Model model, String name){
        model.addAttribute("msg",name);
        return "test";
    }
}
