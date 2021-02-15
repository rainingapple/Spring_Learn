package cn.rainingapple.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class MyController {

    @RequestMapping("/jumplogin")
    public String jumplogin(){
        System.out.println("测试中");
        return "redirect:/login.jsp";
    }

    @RequestMapping("/login")
    public String login(HttpSession httpSession,String username,String pwd){
        httpSession.setAttribute("username",username);
        httpSession.setAttribute("pwd",pwd);
        return "success";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession httpSession){
        httpSession.invalidate();
        return "redirect:/login.jsp";
    }
}
