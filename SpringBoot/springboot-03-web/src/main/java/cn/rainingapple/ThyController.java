package cn.rainingapple;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;

@Controller
public class ThyController {

    @RequestMapping("/hello")
    public String test(){
        return "hello";
    }

    @RequestMapping("/test1")
    public String test1(Model model){
        model.addAttribute("msg","测试一");
        return "test1";
    }

    @RequestMapping("/test2")
    public String test2(Model model){
        model.addAttribute("str","<h1>测试二</h1>");
        model.addAttribute("stus", Arrays.asList("张三","李四"));
        return "test2";
    }

}
