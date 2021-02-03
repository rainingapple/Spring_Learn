import cn.rainingapple.dao.UserMapper;
import cn.rainingapple.pojo.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class MyTest {
    @Test
    public void test(){
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        UserMapper userDao = context.getBean("UserDao", UserMapper.class);
        List<User> selectuser = userDao.selectuser();
        System.out.println(selectuser);
    }
}
