import cn.rainingapple.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest_Diy {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans_diy.xml");
        UserService serviceImpl = context.getBean("userserviceimpl", UserService.class);
        serviceImpl.add();
        serviceImpl.delete();
        serviceImpl.modify();
        serviceImpl.select();
    }
}
