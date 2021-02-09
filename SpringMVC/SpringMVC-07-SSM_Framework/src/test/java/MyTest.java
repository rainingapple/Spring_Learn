import cn.rainingapple.pojo.Books;
import cn.rainingapple.service.BookService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class MyTest {

    @Test
    public void testadduser(){
        ApplicationContext context = new ClassPathXmlApplicationContext("mybatis-spring.xml");
        BookService bookServiceImpl = context.getBean("BookServiceImpl", BookService.class);
        int addBook = bookServiceImpl.addBook(new Books(1, "测试跑路", 9, "啦啦啦啦"));
        System.out.println(addBook);
    }

    @Test
    public void testdeleteuser(){
        ApplicationContext context = new ClassPathXmlApplicationContext("mybatis-spring.xml");
        BookService bookServiceImpl = context.getBean("BookServiceImpl", BookService.class);
        int bookById = bookServiceImpl.deleteBookById(3);
        System.out.println(bookById);
    }

    @Test
    public void testupdateuser(){
        ApplicationContext context = new ClassPathXmlApplicationContext("mybatis-spring.xml");
        BookService bookServiceImpl = context.getBean("BookServiceImpl", BookService.class);
        int book = bookServiceImpl.updateBook(new Books(4, "测试跑路", 99, "省略"));
        System.out.println(book);
    }

    @Test
    public void testselectuserbyid(){
        ApplicationContext context = new ClassPathXmlApplicationContext("mybatis-spring.xml");
        BookService bookServiceImpl = context.getBean("BookServiceImpl", BookService.class);
        Books book = bookServiceImpl.queryBookById(2);
        System.out.println(book);
    }

    @Test
    public void testselectuser(){
        ApplicationContext context = new ClassPathXmlApplicationContext("mybatis-spring.xml");
        BookService bookServiceImpl = context.getBean("BookServiceImpl", BookService.class);
        List<Books> selectbooks = bookServiceImpl.queryAllBook();
        for (Books books : selectbooks) {
            System.out.println(books);
        }
    }
}
