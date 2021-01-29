import cn.rainingapple.Pojo.Annotation_based.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest_Annotation {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans_Annotation.xml");
        Person person = context.getBean("person", Person.class);
        person.getCat().say();
        person.getDog().say();
    }
}
