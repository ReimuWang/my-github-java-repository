package org.reimuwang.testspringboot.test;

import lombok.Data;

import java.util.List;

@Data
public class TestEntity {

    private String id;

    private List<Integer> types;
}
