import cn.rainingapple.dynamic_proxy.Generate_InvocationHandler;
import cn.rainingapple.dynamic_proxy.Host;
import cn.rainingapple.dynamic_proxy.Rent;

public class MyTest_GD {
    public static void main(String[] args) {
        Generate_InvocationHandler generate_invocationHandler = new Generate_InvocationHandler();
        generate_invocationHandler.setTarget(new Host());
        Rent proxy = (Rent) generate_invocationHandler.getProxy();
        proxy.rent();
    }
}
