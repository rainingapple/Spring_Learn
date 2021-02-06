package cn.rainingapple.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Test2 {

    @RequestMapping("/test4")
    public String test4(ModelMap map){
        map.addAttribute("msg","23333");
        return "test";
    }

}
