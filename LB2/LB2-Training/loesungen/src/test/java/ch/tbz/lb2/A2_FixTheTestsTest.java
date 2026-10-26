package ch.tbz.lb2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// LÖSUNG A2
class A2_FixTheTestsTest {

    private final Calculator calculator = new Calculator();
    private final DiscountCalculator discount = new DiscountCalculator();
    private Account account;

    // g) Fix: kein static-Feld mehr, sondern vor JEDEM Test ein neues Konto.
    //    Dann braucht es auch @TestMethodOrder / @Order nicht mehr.
    @BeforeEach
    void setUp() {
        account = new Account(1, "Muster", 100);
    }

    // a) Reihenfolge ist assertEquals(EXPECTED, ACTUAL).
    //    Vertauscht läuft es zwar, aber die Fehlermeldung wäre falsch ("expected 7 but was 5").
    @Test
    void a_add() {
        assertEquals(5, calculator.add(2, 3));
    }

    // b) 0.1 + 0.2 = 0.30000000000000004 -> bei Kommazahlen eine Toleranz (delta) angeben
    @Test
    void b_addDecimals() {
        assertEquals(0.3, calculator.add(0.1, 0.2), 0.0001);
    }

    // c) ab 25'000 gibt es 8.5 %, nicht 7 %
    @Test
    void c_discountFrom25000() {
        assertEquals(8.5, discount.getDiscountPercent(25_000));
    }

    // d) divide wirft eine ArithmeticException, keine NullPointerException
    @Test
    void d_divideByZero() {
        assertThrows(ArithmeticException.class, () -> calculator.divide(1, 0));
    }

    // e) zu viel abheben -> withdraw gibt false zurück und der Kontostand bleibt gleich
    @Test
    void e_withdrawTooMuch() {
        Account a = new Account(2, "Gates", 100);
        assertFalse(a.withdraw(500));
        assertEquals(100, a.getBalance());
    }

    // f) ein Test ohne Assertion prüft nichts
    @Test
    void f_multiply() {
        assertEquals(12, calculator.multiply(3, 4));
    }

    @Test
    void g1_deposit() {
        account.deposit(50);
        assertEquals(150, account.getBalance());
    }

    @Test
    void g2_newAccountHas100() {
        assertEquals(100, account.getBalance());
    }
}
