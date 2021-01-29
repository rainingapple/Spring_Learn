import cn.rainingapple.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest_scope {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans_scope.xml");
        User user = context.getBean("User", User.class);
        User user1 = context.getBean("User", User.class);
        System.out.println(user==user1);
    }
}
