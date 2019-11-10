package org.reimuwang.testspring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {
        System.out.println(new ClassPathXmlApplicationContext("applicationContext.xml").getBean(TestOut.class).getTest());
    }
}
