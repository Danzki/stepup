package com.danzki;

public class TrafficLight {
    Light currentlight;

    public TrafficLight() {
        this.currentlight = Light.RED;
    }

    public Light next() {
        this.currentlight = this.currentlight.getNext();
        return this.currentlight;
    }
}
