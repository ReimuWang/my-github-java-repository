package org.reimuwang.testspring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        Arrays.asList(ac.getBeanDefinitionNames()).forEach(e -> System.out.println(e));
    }
}
