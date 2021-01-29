import cn.rainingapple.config.MyConfig;
import cn.rainingapple.pojo.User_JavaConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyTest_JavaConfigure {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        User_JavaConfig user = context.getBean("user", User_JavaConfig.class);
        System.out.println(user.toString());
    }
}
