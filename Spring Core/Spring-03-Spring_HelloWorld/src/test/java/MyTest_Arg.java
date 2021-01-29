import cn.rainingapple.DI_Test.Apple_Arg;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest_Arg {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans_Arg.xml");
        Apple_Arg apple = (Apple_Arg) context.getBean("Apple");
        apple.show();
    }
}
