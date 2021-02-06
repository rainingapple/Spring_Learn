package cn.rainingapple.controller;

import cn.rainingapple.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class TestJson3 {
    @RequestMapping("/json5")
    public String test1() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();
        return mapper.writeValueAsString(date);
    }

    @RequestMapping("/json6")
    public String test2() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        mapper.setDateFormat(sdf);
        Date date = new Date();
        return mapper.writeValueAsString(date);
    }

    @RequestMapping("/json7")
    public String test3() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();
        return JsonUtils.getJson(date);
    }
}
