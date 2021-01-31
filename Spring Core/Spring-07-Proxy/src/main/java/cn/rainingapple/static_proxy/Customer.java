package cn.rainingapple.static_proxy;

public class Customer {

    Rent rent;

    public void buy(){
        rent = new Agency();
        rent.rent();
        System.out.println("我是客户我要买房，我通过中介买房");
    }
}
