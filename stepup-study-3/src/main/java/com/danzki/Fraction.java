package com.danzki;

import com.danzki.annotations.Cache;
import com.danzki.annotations.Mutator;

public class Fraction implements Fractionable {
    private int num;
    private int denum;

    public Fraction(int num, int denum) {
        if (denum == 0)
            throw new IllegalArgumentException("denum cannot be zero");
        this.num = num;
        this.denum = denum;
    }

    @Override
    @Cache(time = 1000)
    public double doubleValue() {
        System.out.println("Original doubleValue call");
        return (double) num / denum;
    }

    @Mutator
    public void setNum(int num) {
        this.num = num;
    }

    @Mutator
    public void setDenum(int denum) {
        if (denum == 0)
            throw new IllegalArgumentException("denum cannot be zero");
        this.denum = denum;
    }
}
