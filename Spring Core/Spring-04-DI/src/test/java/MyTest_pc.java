import cn.rainingapple.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest_pc {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans_pc.xml");
        User user = context.getBean("User", User.class);
        System.out.println(user.toString());
    }
}
