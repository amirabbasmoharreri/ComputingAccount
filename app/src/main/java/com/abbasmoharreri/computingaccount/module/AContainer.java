package com.abbasmoharreri.computingaccount.module;

import java.util.List;

public class AContainer {

    private List<Integer> workId;
    private String name;
    private int sumPrice;

    public List<Integer> getWorkId() {
        return workId;
    }

    public AContainer setWorkId(List<Integer> workId) {
        this.workId = workId;
        return this;
    }

    public String getName() {
        return name;
    }

    public AContainer setName(String name) {
        this.name = name;
        return this;
    }

    public int getSumPrice() {
        return sumPrice;
    }

    public AContainer setSumPrice(int sumPrice) {
        this.sumPrice = sumPrice;
        return this;
    }
}
