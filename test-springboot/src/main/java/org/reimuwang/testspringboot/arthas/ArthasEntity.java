package org.reimuwang.testspringboot.arthas;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ArthasEntity {

    private Integer seed;

    private String testStr;

    private Long testLong;

    private List<Integer> testIntegerList;

    public static ArthasEntity init(Integer seed) {
        ArthasEntity result = new ArthasEntity();
        result.seed = seed;
        result.setOthers();
        return result;
    }

    private void setOthers() {
        this.testStr = seed + "";
        this.testLong = 1L * seed;
        this.testIntegerList = new ArrayList<>();
        for (int i = 0; i < seed; i++) {
            this.testIntegerList.add(i);
        }
    }

    public void refreshSeed(int seedChange) {
        this.seed = this.seed + seedChange;
        this.setOthers();
    }
}
