package com.danzki;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

@Configuration("com.danzki")
public class Config {
    List<Integer> usedValues;

    private int getRandomValue(int min, int max) {
        Random rand = new Random();
        int value = min + rand.nextInt((max - min) + 1);
        return value;
    }
    @Bean
    String hello() {
        return "Hello world";
    }

    @Bean
    Date myDate() {
        return new Date();
    }

    @Bean(name = "predicate")
    Predicate getPredicate(@Value("4") Integer num) {
        return new Predicate() {
            @Override
            public boolean test(Object o) {
                Integer num = (Integer) o;
                if (num >= 2 && num <= 5) {
                    return true;
                }
                return false;
            }
        };
    }

    @Bean(name = "min")
    @Qualifier("getMin")
    int getMin() {
        return 2;
    }

    @Bean(name = "max")
    @Qualifier("getMax")
    int getMax() {
        return 5;
    }

    @Bean(name = "random")
    @Scope("prototype")
    int getRandom(@Qualifier("getMin") int min, @Qualifier("getMax") int max) {
        int value = getRandomValue(min, max);
        if (usedValues == null) {
            usedValues = new ArrayList<>();
        }
        while (usedValues.contains(value) && usedValues.size() < (max - min)) {
            value = getRandomValue(min, max);
        }
        usedValues.add(value);
        return value;
    }

    @Bean("feedback1")
    Feedback getFeedback1() {
        var feedback = new Feedback();
        feedback.setFeedback(4);
        return feedback;
    }

    @Bean("feedback2")
    Feedback getFeedback2() {
        var feedback = new Feedback();
        feedback.setFeedback(3);
        return feedback;
    }

    @Bean("feedback3")
    Feedback getFeedback3() {
        var feedback = new Feedback();
        feedback.setFeedback(Integer.valueOf(getRandom(getMin(), getMax())));
        return feedback;
    }

    @Bean("bestfeedback")
    BestFeedback bestFeedback() {
        return new BestFeedback();
    }

    @Bean("trafficlight")
    TrafficLight getTrafficLight() {
        return new TrafficLight();
    }

}
