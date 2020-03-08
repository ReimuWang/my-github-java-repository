package org.reimuwang.testspring;

import org.reimuwang.testspring.test.Test2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigDemo {

//    @Bean
//    public Test1 test1() {
//        return new Test1("haha");
//    }

    @Bean
    public Test2 test2() {
        return new Test2();
    }
}
