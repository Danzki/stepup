package com.danzki;

import org.junit.jupiter.api.*;

public class UtilsTests {

    private static TestClass original;
    private static TestInterface originalProxy;

    @BeforeEach
    void getNewActual() {
        original = new TestClass();
        originalProxy = Utils.cache(original);
    }

    @Test
    @DisplayName("Выполняем оригинальный метод 4 раза, вернется 4.")
    void checkOriginalMethod() {
        int actual = original.increamentCounter();
        actual = original.increamentCounter();
        actual = original.increamentCounter();
        actual = original.increamentCounter();
        Assertions.assertEquals(4, actual);
    }

    @Test
    @DisplayName("Выполняем метод 4 раза, вернется 1.")
    void checkCachedMethod() {
        int actual = originalProxy.increamentCounter();
        actual = originalProxy.increamentCounter();
        actual = originalProxy.increamentCounter();
        actual = originalProxy.increamentCounter();
        Assertions.assertEquals(1, actual);
    }

}
