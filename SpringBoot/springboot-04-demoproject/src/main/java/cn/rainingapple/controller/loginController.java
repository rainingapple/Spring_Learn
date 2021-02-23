package cn.rainingapple.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class loginController{
    @RequestMapping("/user/login")
    public String login(@RequestParam("username") String name, @RequestParam("pwd")String pwd, Model model, HttpSession session){
        if(name.equals("admin")&&pwd.equals("123456")){
            model.addAttribute("msg","成功");
            session.setAttribute("loginname","admin");
            return "redirect:/main.html";
        }
        else {
            model.addAttribute("msg","失败");
            return "index";
        }
    }
}
