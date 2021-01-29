import cn.rainingapple.pojo.Service.UserServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest_Dao {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans_dao.xml");
        UserServiceImpl service = (UserServiceImpl) context.getBean("service");
        service.getname();
    }
}
