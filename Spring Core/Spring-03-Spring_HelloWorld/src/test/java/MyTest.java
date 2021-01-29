import cn.rainingapple.pojo.Hello;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {
    public static void main(String[] args) {
        //获取Spring的上下文对象
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        //从容器中取出对象
        Hello hello = (Hello) context.getBean("Hello");
        //正常调用执行方法
        hello.show();
    }
}
