package com.danzki;

import org.junit.jupiter.api.*;

public class CacheUtilsTests {

    private static TestClass original;
    private static TestInterface originalProxy;

    private long timeOutShort = 200;
    private long timeOutLong = 1500;

    @BeforeEach
    void getNewActual() {
        original = new TestClass();
        originalProxy = CacheUtils.cache(original);
    }

    @Test
    @DisplayName("Выполняем оригинальный метод 4 раза, вернется 4.")
    void checkOriginalMethod() {
        original.resetCounter();
        Integer actual = original.increamentCounter();
        actual = original.increamentCounter();
        actual = original.increamentCounter();
        actual = original.increamentCounter();
        Assertions.assertEquals(4, actual);
    }

    @Test
    @DisplayName("Выполняем метод 4 раза, вернется 1.")
    void checkCachedMethod() {
        originalProxy.resetCounter();
        Integer actual = originalProxy.increamentCounter();
        actual = originalProxy.increamentCounter();
        actual = originalProxy.increamentCounter();
        actual = originalProxy.increamentCounter();
        Assertions.assertEquals(1, actual);
    }

    @Test
    @DisplayName("Выполняем с задержкой меньше времени жизни метод 4 раза, вернется 1.")
    void checkCachedMethodSuccessDelay() {
        originalProxy.resetCounter();
        Integer actual = originalProxy.increamentCounter();
        try {
            Thread.sleep(timeOutShort);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        actual = originalProxy.increamentCounter();
        actual = originalProxy.increamentCounter();
        actual = originalProxy.increamentCounter();
        Assertions.assertEquals(1, actual);
    }

    @Test
    @DisplayName("Выполняем с задержкой больше времени жизни метод 4 раза, вернется 2.")
    void checkCachedMethodErrorDelay() {
        originalProxy.resetCounter();
        Integer actual = originalProxy.increamentCounter();
        actual = originalProxy.increamentCounter();
        try {
            Thread.sleep(timeOutLong);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        actual = originalProxy.increamentCounter();
        actual = originalProxy.increamentCounter();
        Assertions.assertEquals(2, actual);
    }

}
