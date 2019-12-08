package org.reimuwang.testspringboot.test;

import lombok.Data;

@Data
public class TestEntity {

    private String id;

    private Integer value;

    public TestEntity(String id, Integer value) {
        this.id = id;
        this.value = value;
    }
}
