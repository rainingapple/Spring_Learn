package cn.rainingapple.controller;

import cn.rainingapple.dao.DepartmentDao;
import cn.rainingapple.dao.EmployeeDao;
import cn.rainingapple.pojo.Department;
import cn.rainingapple.pojo.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Collection;


@Controller
public class EmployeeController {

    @Autowired
    EmployeeDao employeeDao;
    @Autowired
    DepartmentDao departmentDao;

    @RequestMapping("/emps")
    public String list(Model model){
        model.addAttribute("emps",employeeDao.getAll());
        return "/emp/list";
    }

    @GetMapping("/add")
    public String add(Model model){
        model.addAttribute("departments",departmentDao.getDepartment());
        return "/emp/add";
    }

    @PostMapping("/add")
    public String addpost(Employee employee){
        employeeDao.addemployee(employee);
        return "redirect:/emps";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id,Model model){
        Employee employeeByID = employeeDao.getEmployeeById(id);
        model.addAttribute("empByID", employeeByID);
        //查出所有的部门信息,添加到departments中,用于前端接收
        Collection<Department> departments = departmentDao.getDepartment();
        model.addAttribute("departments", departments);
        return "/emp/edit";
    }

    @PostMapping("/edit")
    public String EditEmp(Employee employee) {
        employeeDao.addemployee(employee);//添加一个员工
        return "redirect:/emps";//添加完成重定向到/emps,刷新列表
    }

    @GetMapping("/del/{id}")
    public String deleteemp(@PathVariable("id") int id){
        employeeDao.delete(id);
        return "redirect:/emps";
    }

    @RequestMapping("/user/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/index.html";
    }
}
