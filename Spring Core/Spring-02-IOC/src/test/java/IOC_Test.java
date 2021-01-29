import cn.rainingapple.IOC.UserDaoImplHello;
import cn.rainingapple.IOC.UserService;
import cn.rainingapple.IOC.UserServiceImpl_Use_IOC;

public class IOC_Test {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl_Use_IOC();
        userService.setname(new UserDaoImplHello());
        userService.getname();
    }
}
