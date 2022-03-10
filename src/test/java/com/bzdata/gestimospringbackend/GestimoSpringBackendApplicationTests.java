package com.bzdata.gestimospringbackend;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GestimoSpringBackendApplicationTests {

    Cal underTest = new Cal();

    @Test
    void itShoulAddNumbers() {
        // given
        int nuberOne = 20;
        int numberTwo = 30;

        // when
        int result = underTest.add(nuberOne, numberTwo);
        // then
        assertEquals(50, result);
    }

    class Cal {
        int add(int a, int b) {
            return a + b;
        }
    }
}
