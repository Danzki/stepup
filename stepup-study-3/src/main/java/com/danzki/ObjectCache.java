package com.danzki;

public class ObjectCache {
    final private Object value;
    final private long exprTime;

    public ObjectCache(Object value, Clock clock, long timeout) {
        this.value = value;
        this.exprTime = clock.currentMillis() + timeout;
    }

    public ObjectCache(Object value, long timeout) {
        this.value = value;
        this.exprTime = System.currentTimeMillis() + timeout;
    }

    public Object getValue() {
        return value;
    }

    public Boolean isLive(long currentTimeMillis) {
        return this.exprTime > currentTimeMillis;
    }
}
