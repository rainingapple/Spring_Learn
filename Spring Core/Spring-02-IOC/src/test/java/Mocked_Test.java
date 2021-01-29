import cn.rainingapple.Mocked.UserService;
import cn.rainingapple.Mocked.UserServiceImpl_Ori_Mocked;

public class Mocked_Test {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl_Ori_Mocked();
        userService.getname();
    }
}
