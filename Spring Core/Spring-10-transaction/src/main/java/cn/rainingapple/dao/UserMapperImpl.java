package cn.rainingapple.dao;

import cn.rainingapple.pojo.User;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.util.List;

public class UserMapperImpl implements UserMapper {
    private SqlSessionTemplate sqlSessionTemplate;

    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public int adduser(User user) {
        return sqlSessionTemplate.getMapper(UserMapper.class).adduser(user);
    }

    @Override
    public int deleteuser(int id) {
        return sqlSessionTemplate.getMapper(UserMapper.class).deleteuser(id);
    }

    @Override
    public List<User> selectuser() {
        User user = new User(8,"不该存在的8","66666");
        adduser(user);
        deleteuser(8);
        return sqlSessionTemplate.getMapper(UserMapper.class).selectuser();
    }
}
