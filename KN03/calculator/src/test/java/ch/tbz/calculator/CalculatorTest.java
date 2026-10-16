package ch.tbz.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    @DisplayName("Zwei positive Zahlen addieren")
    void addPositiveNumbers() {
        assertEquals(5, calculator.add(2, 3));
    }

    @Test
    void addNegativeNumbers() {
        assertEquals(-5, calculator.add(-2, -3));
    }

    @Test
    void addDecimalNumbers() {
        assertEquals(0.3, calculator.add(0.1, 0.2), 0.0001);
    }

    @Test
    void subtractNumbers() {
        assertEquals(6, calculator.subtract(10, 4));
    }

    @Test
    void subtractToNegativeResult() {
        assertEquals(-6, calculator.subtract(4, 10));
    }

    @Test
    void multiplyNumbers() {
        assertEquals(12, calculator.multiply(3, 4));
    }

    @Test
    void multiplyWithZero() {
        assertEquals(0, calculator.multiply(99, 0));
    }

    @Test
    void multiplyNegativeNumbers() {
        assertEquals(6, calculator.multiply(-2, -3));
    }

    @Test
    void divideNumbers() {
        assertEquals(2.5, calculator.divide(5, 2));
    }

    @Test
    void divideByZeroThrowsException() {
        assertThrows(ArithmeticException.class, () -> calculator.divide(5, 0));
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1, 2",
            "10, -5, 5",
            "0, 0, 0",
            "1000000, 1, 1000001"
    })
    void addManyValues(double a, double b, double expected) {
        assertEquals(expected, calculator.add(a, b));
    }
}
