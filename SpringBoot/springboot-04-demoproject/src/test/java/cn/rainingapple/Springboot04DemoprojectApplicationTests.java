package cn.rainingapple;

import cn.rainingapple.dao.DepartmentDao;
import cn.rainingapple.dao.EmployeeDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Springboot04DemoprojectApplicationTests {

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    EmployeeDao employeeDao;

    @Test
    void contextLoads() {
        System.out.println(departmentDao.getDepartment());
        System.out.println(departmentDao.getDepartmentById(2));
        System.out.println(employeeDao.getAll());
        System.out.println(employeeDao.getEmployeeById(1));
    }

}
