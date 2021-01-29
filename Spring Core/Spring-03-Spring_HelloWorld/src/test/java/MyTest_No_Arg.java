import cn.rainingapple.DI_Test.Apple_No_Arg;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest_No_Arg {
    public static void main(String[] args) {
        //这里通过打断点可以发现这里使在生成context的时候就产生了所有的对象
        ApplicationContext context = new ClassPathXmlApplicationContext("beans_No_Arg.xml");
        Apple_No_Arg apple = (Apple_No_Arg) context.getBean("Apple");
        apple.show();
    }
}
