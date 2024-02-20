package com.danzki;

import org.junit.jupiter.api.*;

public class CacheUtilsTests {
    static class TestClock implements Clock {
        long time;

        public TestClock(long time) {
            this.time = time;
        }

        @Override
        public long currentMillis() {
            return this.time;
        }
    }

    @Test
    @DisplayName("Тест с использованием короткого интервала")
    void checkShortInterval() {
        var clock = new TestClock(1L);
        var ci = (TestInterface) new CacheUtils(clock).cache(new TestClass());

        var v1 = ci.testMethod();
        var v2 = ci.testMethod();
        Assertions.assertEquals(v1, v2);
    }
}
