package cn.rainingapple.DI_Test;

public class Apple_Arg {
    String Apple;

    public Apple_Arg(String apple) {
        Apple = apple;
        System.out.println(Apple);
    }

    public void setApple(String apple) {
        Apple = apple;
    }

    public void show(){
        System.out.println("阿巴阿巴阿巴阿巴");
    }
}
