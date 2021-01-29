package cn.rainingapple.pojo.Service;

import cn.rainingapple.pojo.Dao.UserDao;

public class UserServiceImpl implements UserService {

    UserDao userDao;

    @Override
    public void getname() {
        userDao.getname();
    }

    @Override
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
