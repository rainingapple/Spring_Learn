package cn.rainingapple.dao;

//部门dao

import cn.rainingapple.pojo.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DepartmentDao {
    //模拟数据库的数据

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getDepartment(){
        String sql = "select * from department";
        return jdbcTemplate.queryForList(sql);
    }

    public Department getDepartmentById(Integer id){
        String sql = "select departmentname from department where id=?";
        return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(Department.class),id);
    }

}
