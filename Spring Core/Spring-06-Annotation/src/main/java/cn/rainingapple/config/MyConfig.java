package cn.rainingapple.config;

import cn.rainingapple.pojo.User_JavaConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {
    @Bean
    public User_JavaConfig user(){
        return new User_JavaConfig();
    }
}
