package ch.tbz.lb2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// TODO A1: In dieser Klasse fehlen ALLE JUnit-Annotations (und ihre Imports).
//  Ziel nach dem Fix: 4 Tests grün, 1 Test übersprungen.
//  1. setUp() soll vor JEDEM Test laufen (neuer Calculator pro Test)
//  2. initAll() soll EINMAL vor allen Tests laufen (Achtung: was braucht die Methode dafür?)
//  3. Alle Methoden, die mit "test" anfangen, sind Tests
//  4. testAdd soll im Testreport "Zwei Zahlen addieren" heissen
//  5. testPercent ist noch nicht fertig und soll übersprungen werden
class A1_AnnotationsTest {

    private Calculator calculator;

    void initAll() {
        System.out.println("Start der Tests");
    }

    void setUp() {
        calculator = new Calculator();
    }

    void testAdd() {
        assertEquals(5, calculator.add(2, 3));
    }

    void testSubtract() {
        assertEquals(-1, calculator.subtract(2, 3));
    }

    void testMultiply() {
        assertEquals(6, calculator.multiply(2, 3));
    }

    void testDivideByZero() {
        assertThrows(ArithmeticException.class, () -> calculator.divide(1, 0));
    }

    void testPercent() {
        // Feature gibt es noch nicht
        throw new UnsupportedOperationException("kommt später");
    }
}
