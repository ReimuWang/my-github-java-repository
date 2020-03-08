package org.reimuwang.testspring;

import org.reimuwang.testspring.test.Test2;
import org.springframework.beans.factory.annotation.Autowired;

public class Test1 {

    private String name;

    @Autowired
    private Test2 t2;

    public Test1(String name) {
        this.name = name;
    }
}
