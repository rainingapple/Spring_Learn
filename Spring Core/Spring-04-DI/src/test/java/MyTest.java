import cn.rainingapple.Student;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        Student student = (Student) context.getBean("Student");
        System.out.println(student.toString());

        /* 输出为
        Student{
        name='张三',
        address=Address{address='中南大学'},
        books=[飘, 霍乱时期的爱情, 源氏物语],
        hobbys=[唱, 跳, rap, 练球],
        card={身份证=1234654654321, 银行卡=454645646, 校园卡=613248432},
        games=[DOTA, 炉石传说, 打地鼠],
        wife='null',
        info={user=root, password=123456}
        }*/
    }
}
