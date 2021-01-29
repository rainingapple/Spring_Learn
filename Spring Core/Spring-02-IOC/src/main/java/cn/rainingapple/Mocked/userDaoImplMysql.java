package cn.rainingapple.Mocked;

public class userDaoImplMysql implements UserDao{
    @Override
    public void getname() {
        System.out.println("Mysql");
    }
}
