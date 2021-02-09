package cn.rainingapple.dao;

import cn.rainingapple.pojo.Books;

import java.util.List;

public interface BookMapper {

    int addBook(Books book);
    int deleteBookById(int id);
    int updateBook(Books books);
    Books queryBookById(int id);
    List<Books> selectbooks();
}
