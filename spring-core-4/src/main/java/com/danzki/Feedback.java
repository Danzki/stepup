package com.danzki;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class Feedback {
    private String text;
    private int value;

    private void setText(int value) {
        switch (this.value) {
            case 2: this.text = "Все очень плохо";
                break;
            case 3: this.text = "Сойдёт";
                break;
            case 4: this.text = "Очень хорошо";
                break;
            case 5: this.text = "Отлично!";
                break;
            default: this.text = "Сложно сказать";
        }
    }
    public Feedback() {
    }

    public int getValue() {
        return value;
    }

    public void setFeedback(int value) {
        this.value = value;
        setText(this.value);
    }

    @Override
    public String toString() {
        return "Текст: " + text + ". Оценка: " + value + ".";
    }
}
