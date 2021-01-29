import cn.rainingapple.DI_Test.Apple_Arg;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest_Configure_test {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans_configure_test.xml");
        //Apple_Arg apple = (Apple_Arg) context.getBean("Apple");
        //Apple_Arg apple = (Apple_Arg) context.getBean("Green_Apple");
        //Apple_Arg apple = (Apple_Arg) context.getBean("u1");
        //Apple_Arg apple = (Apple_Arg) context.getBean("u2");
        //Apple_Arg apple = (Apple_Arg) context.getBean("u3");
        Apple_Arg apple = (Apple_Arg) context.getBean("u4");
        apple.show();
    }
}
