package com.danzki;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;

public class BestFeedback {
    @Autowired List<Feedback> feedbacks;

    public Feedback getBest() {
        var maxValue = feedbacks.stream()
                .max(new Comparator<Feedback>() {
                    @Override
                    public int compare(Feedback o1, Feedback o2) {
                        return o1.getValue() - o2.getValue();
                    }
                })
                .get();
        return maxValue;
    }
}
