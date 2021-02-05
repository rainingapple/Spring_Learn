package cn.rainingapple.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RestFulController {

    @GetMapping("/rest1/{a}/{b}")
    public String test1(@PathVariable int a, @PathVariable int b, Model model){
        model.addAttribute("msg",a+b);
        return "test";
    }

}
