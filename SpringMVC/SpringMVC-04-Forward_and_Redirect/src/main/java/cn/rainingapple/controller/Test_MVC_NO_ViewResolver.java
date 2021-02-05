package cn.rainingapple.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
//测试前，请先将视图解析器注释掉
public class Test_MVC_NO_ViewResolver {

    @GetMapping("/MVCNO/test1")
    public String test1(){
        return "/WEB-INF/jsp/success.jsp";
    }

    @GetMapping("/MVCNO/test2")
    public String test2(){
        return "forward:/WEB-INF/jsp/success.jsp";
    }

    @GetMapping("/MVCNO/test3")
    public String test3(){
        return "redirect:/index.jsp";
    }
}
