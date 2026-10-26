package ch.tbz.lb2;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// LÖSUNG A1
class A1_AnnotationsTest {

    private Calculator calculator;

    @BeforeAll                       // einmal vor allen Tests -> Methode MUSS static sein
    static void initAll() {
        System.out.println("Start der Tests");
    }

    @BeforeEach                      // vor jedem Test -> jeder Test bekommt einen frischen Calculator
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    @DisplayName("Zwei Zahlen addieren")
    void testAdd() {
        assertEquals(5, calculator.add(2, 3));
    }

    @Test
    void testSubtract() {
        assertEquals(-1, calculator.subtract(2, 3));
    }

    @Test
    void testMultiply() {
        assertEquals(6, calculator.multiply(2, 3));
    }

    @Test
    void testDivideByZero() {
        assertThrows(ArithmeticException.class, () -> calculator.divide(1, 0));
    }

    @Test
    @Disabled("Feature kommt später")
    void testPercent() {
        throw new UnsupportedOperationException("kommt später");
    }
}
