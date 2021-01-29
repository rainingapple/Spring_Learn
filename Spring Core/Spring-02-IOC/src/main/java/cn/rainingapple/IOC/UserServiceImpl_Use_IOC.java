package cn.rainingapple.IOC;

public class UserServiceImpl_Use_IOC implements UserService {
    UserDao userDao;

    @Override
    public void getname() {
        userDao.getname();
    }

    @Override
    public void setname(UserDao userDao) {
        this.userDao = userDao;
    }
}
