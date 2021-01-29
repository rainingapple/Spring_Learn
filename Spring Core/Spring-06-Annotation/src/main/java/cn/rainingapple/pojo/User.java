package cn.rainingapple.pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("user")
@Scope("prototype")
public class User {
    @Value("张三")
    private String name;

    public String getName() {
        return name;
    }
}
