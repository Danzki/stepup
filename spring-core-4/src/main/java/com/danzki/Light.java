package com.danzki;

public enum Light {
    GREEN {
        public Light getNext() {return YELLOW;}
    },
    YELLOW{
        public Light getNext() {return RED;}
    },
    RED{
        public Light getNext() {return GREEN;}
    };

    public abstract Light getNext();
}
