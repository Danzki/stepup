package com.danzki;

import com.danzki.annotations.Cache;
import com.danzki.annotations.Mutator;

public class TestClass implements TestInterface {
    private int counter;

    public TestClass() {
        this.counter = 0;
    }

    @Override
    @Cache(time = 1000)
    public int increamentCounter() {
        this.counter++;
        return this.counter;
    }

    @Override
    @Mutator
    public void resetCounter() {
        this.counter = 0;
    }
}
