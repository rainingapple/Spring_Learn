package cn.rainingapple.controller;

import cn.rainingapple.mapper.MybatisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jdbc")
public class JdbcController {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    MybatisMapper mybatisMapper;

    @RequestMapping("/list")
    public List<Map<String,Object>> selectuser(){
        String sql = "Select * from bootjdbc";
        //List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        List<Map<String, Object>> maps = mybatisMapper.selectuser();
        return maps;
    }

    //新增一个用户
    @GetMapping("/add")
    public String addUser(){
        //插入语句，注意时间问题
        String sql = "insert into bootjdbc(name) values ('狂神说')";
        jdbcTemplate.update(sql);
        //查询
        return "addOk";
    }

    //修改用户信息
    @GetMapping("/update")
    public String updateUser(){
        //插入语句
        String sql = "update bootjdbc set name=233 where name='狂神说'";
        jdbcTemplate.update(sql);
        return "updateOk";
    }

    //删除用户
    @GetMapping("/delete")
    public String delUser(){
        //插入语句
        String sql = "delete from bootjdbc where name=233";
        jdbcTemplate.update(sql);
        //查询
        return "deleteOk";
    }

}
