package cn.rainingapple.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface MybatisMapper {
    @Select("Select * from bootjdbc")
    public List<Map<String,Object>> selectuser();
}
