package cn.rainingapple.controller;

import cn.rainingapple.pojo.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class TestJson2 {

    @RequestMapping("/json3")
    public String test1() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        User user = new User(1,"拉拉",15);
        return mapper.writeValueAsString(user);
    }

    @RequestMapping("/json4")
    public String test2() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        User user1 = new User(1,"拉拉",15);
        User user2 = new User(1,"拉拉",15);
        User user3 = new User(1,"拉拉",15);
        User user4 = new User(1,"拉拉",15);
        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);
        list.add(user3);
        list.add(user4);
        return mapper.writeValueAsString(list);
    }
}
