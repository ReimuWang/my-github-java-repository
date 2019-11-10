package org.reimuwang.testspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestOut {

    @Autowired
    public Test test;

    public Test getTest() {
        return test;
    }
}
