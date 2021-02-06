package cn.rainingapple.controller;

import cn.rainingapple.pojo.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestJson1 {
    @RequestMapping("/json1")
    @ResponseBody
    public String test1() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        User user = new User(1,"拉拉",15);
        return mapper.writeValueAsString(user);
    }

    @RequestMapping(value = "/json2",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String test2() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        User user = new User(1,"拉拉",15);
        return mapper.writeValueAsString(user);
    }
}
