package cn.rainingapple.dao;

import cn.rainingapple.pojo.User;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.util.List;

public class UserMapperImpl2 extends SqlSessionDaoSupport implements UserMapper{
    @Override
    public List<User> selectuser() {
        return getSqlSession().getMapper(UserMapper.class).selectuser();
    }
}
