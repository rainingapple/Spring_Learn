import cn.rainingapple.dynamic_proxy.Host;
import cn.rainingapple.dynamic_proxy.MyInvocationHandler;
import cn.rainingapple.dynamic_proxy.Rent;

public class MyTest_Dynamic {
    public static void main(String[] args) {
        MyInvocationHandler myInvocationHandler = new MyInvocationHandler();
        myInvocationHandler.setRent(new Host());
        Rent proxy = (Rent) myInvocationHandler.getProxy();
        proxy.rent();

    }
}
