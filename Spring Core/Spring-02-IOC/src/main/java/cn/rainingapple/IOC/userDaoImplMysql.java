package cn.rainingapple.IOC;

public class userDaoImplMysql implements UserDao {
    @Override
    public void getname() {
        System.out.println("Mysql");
    }
}
