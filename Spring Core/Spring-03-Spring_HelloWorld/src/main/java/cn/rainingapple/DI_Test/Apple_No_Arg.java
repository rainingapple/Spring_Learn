package cn.rainingapple.DI_Test;

public class Apple_No_Arg {
    String name;

    public Apple_No_Arg() {
        System.out.println("默认无参构造");
        this.name = "Default";
    }

    public void setName(String name) {
        this.name = name;
    }

    public void show(){
        System.out.println("阿巴阿巴");
    }
}
