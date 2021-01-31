package cn.rainingapple;

public class UserServiceImpl implements UserService{
    @Override
    public void add() {
        System.out.println("加入了一个元素");
    }

    @Override
    public void delete() {
        System.out.println("删除了一个元素");
    }

    @Override
    public void modify() {
        System.out.println("更改了一个元素");
    }

    @Override
    public void select() {
        System.out.println("查询了一个元素");
    }
}
