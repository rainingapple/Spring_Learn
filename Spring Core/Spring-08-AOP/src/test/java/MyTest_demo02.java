import demo2.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest_demo02 {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        UserService serviceImpl = context.getBean("userservice", UserService.class);
        serviceImpl.add();
        serviceImpl.delete();
        serviceImpl.modify();
        serviceImpl.select();
    }
}
