package org.reimuwang.testspring;

import javax.annotation.PostConstruct;

public class Test {

    private Integer p;

    @PostConstruct
    public void init() {
        System.out.println("init被调用，此时p=" + this.p);
    }

    public Test() {
        System.out.println("构造函数被调用");
    }

    public void setP(Integer p) {
        this.p = p;
        System.out.println("p被设置为" + p);
    }

    @Override
    public String toString() {
        return "Test{" +
                "p=" + p +
                '}';
    }
}
