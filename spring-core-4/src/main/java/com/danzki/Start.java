package com.danzki;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.function.Predicate;

public class Start {
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);


//        Создайте бин типа String со значением “Hello world”.
        System.out.println(ctx.getBean("hello"));
//        Создайте бин типа Date хранящий дату и время первого обращения к этому бину.
        System.out.println(ctx.getBean("myDate"));
//        Создайте бин типа Predicate параметризованный типом Integer, который проверяет, что число укладывается в диапазон от 2 до 5 включительно.
        System.out.println(ctx.getBean(Predicate.class).test(8));
//        Создайте два бина типа int, один из которых имеет имя max, а другой —  min. Значения данным бинам задайте произвольное.
        System.out.println(ctx.getBean("min").toString());
        System.out.println(ctx.getBean("max").toString());
//        Создайте бин типа int, который при каждом его запросе возвращает новое случайное значение в диапазоне от 0 до 99.  При этом необходимо обеспечить, чтобы:
//1.     Минимальное и максимальное значения задавались бинами из задачи 4
//2.     Возвращаемые им числа не повторялись до тех пор, пока не использованы все значения в диапазоне от минимального до максимального.
        System.out.println(ctx.getBean("random"));
        System.out.println(ctx.getBean("random"));
        System.out.println(ctx.getBean("random"));
        System.out.println(ctx.getBean("random"));
        System.out.println(ctx.getBean("random"));
//        Создайте класс «Отзыв», состоящий из текста отзыва в виде строки и целочисленной оценки.
//Далее создайте три бина типа «Отзыв» со следующими значениями:
//1.     Текст: «Очень хорошо». Оценка: 4
//2.     Текст: «Сойдёт». Оценка:3
//3.     Текст: «Сложно сказать». Оценка устанавливается бином random задачи 5
        System.out.println(ctx.getBean("feedback1"));
        System.out.println(ctx.getBean("feedback2"));
        System.out.println(ctx.getBean("feedback3"));

//      Создайте бин типа «Отзыв», который будет возвращать тот из созданных в задаче 6 бинов,
//      который имеет самую высокую оценку на момент запроса бина.
        System.out.println(ctx.getBean("bestfeedback", BestFeedback.class).getBest());

//        Разработайте сущность Светофор, которая может использоваться для управления пешеходами и машинами на дороге.
//        При вызове метода next, на экран выводится текст обозначающего текущий цвет, например, «красный» или «зелёный».
        System.out.println(ctx.getBean("trafficlight", TrafficLight.class).next());
        System.out.println(ctx.getBean("trafficlight", TrafficLight.class).next());
        System.out.println(ctx.getBean("trafficlight", TrafficLight.class).next());

    }


}
