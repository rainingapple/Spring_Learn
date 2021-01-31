package cn.rainingapple.static_proxy;

public class Host implements Rent{

    @Override
    public void rent() {
        System.out.println("房主想要卖一个房子");
    }
}
