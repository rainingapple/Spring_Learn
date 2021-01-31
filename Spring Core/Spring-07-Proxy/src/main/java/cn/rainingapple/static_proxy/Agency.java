package cn.rainingapple.static_proxy;

public class Agency implements Rent{

    Host host = new Host();

    @Override
    public void rent() {
        host.rent();
        System.out.println("我是中介，代理房主卖房，收取了一定费用");
    }
}
