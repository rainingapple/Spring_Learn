package cn.rainingapple.Mocked;

public class UserServiceImpl_Ori_Mocked implements UserService {
    //这里使写死的
    UserDao userDao = new UserDaoImplHello();

    @Override
    public void getname() {
        userDao.getname();
    }
}
