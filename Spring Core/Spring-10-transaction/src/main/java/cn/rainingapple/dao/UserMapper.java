package cn.rainingapple.dao;

import cn.rainingapple.pojo.User;

import java.util.List;

public interface UserMapper {
    List<User> selectuser();

    int adduser(User user);

    int deleteuser(int id);
}
