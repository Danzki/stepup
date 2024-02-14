package com.danzki;

public class Main {
    public static void main(String[] args) {
        Fraction fr = new Fraction(2, 3);
        Fractionable num = Utils.cache(fr);
        num.doubleValue();
        num.doubleValue();
        num.doubleValue();
        num.setNum(4);
        num.doubleValue();
        num.doubleValue();
    }
}