package cn.rainingapple.diypointcut;

public class DiyPointCut {
    public void before(){
        System.out.println("Do something before invocation");
    }

    public void after(){
        System.out.println("Do something after invocation");
    }
}
