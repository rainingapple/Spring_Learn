package cn.rainingapple.dao;

import cn.rainingapple.pojo.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
//员工Dao
public class EmployeeDao {
    //模拟数据中的信息
    private static Map<Integer, Employee> employees = null;
    //员工有所属的部门
    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    //增加一个员工
    public void addemployee(Employee employee) {
        String sql="insert into employee values(?,?,?,?,?,?)";
        jdbcTemplate.update(sql,employee.getId(),employee.getLastName(),employee.getEmail(),
                employee.getGender(),employee.getDepartmentid(),employee.getDate());
    }

    //增加一个员工
    public void editemployee(Employee employee) {
        String sql="update employee set departmentid=? where id=?";
        jdbcTemplate.update(sql,employee.getDepartmentid(),employee.getId());
    }

    //查询全部的员工信息
    public List<Map<String, Object>> getAll() {
        String sql = "select *,departmentname from employee,department where employee.departmentid=department.id";
        return jdbcTemplate.queryForList(sql);
    }

    //通过ID查询员工
    public Employee getEmployeeById(Integer id) {
        String sql = "select * from employee where id=?";
        return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(Employee.class),id);
    }

    //通过ID删除员工
    public void delete(Integer id) {
        String sql = "delete from employee where id=?";
        jdbcTemplate.update(sql,id);
    }
}
