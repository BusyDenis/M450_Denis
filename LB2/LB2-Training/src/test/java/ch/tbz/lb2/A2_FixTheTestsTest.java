package ch.tbz.lb2;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

// TODO A2: Der Produktivcode ist KORREKT. Die Fehler sind in den TESTS.
//  Repariere jeden Test (a bis g). Achtung: Manche Tests sind grün und trotzdem falsch!
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class A2_FixTheTestsTest {

    private final Calculator calculator = new Calculator();
    private final DiscountCalculator discount = new DiscountCalculator();
    private static Account account = new Account(1, "Muster", 100);

    // a) grün, aber falsch geschrieben. Warum?
    @Test
    void a_add() {
        assertEquals(calculator.add(2, 3), 5);
    }

    // b) rot
    @Test
    void b_addDecimals() {
        assertEquals(0.3, calculator.add(0.1, 0.2));
    }

    // c) rot (Regeln stehen in DiscountCalculator)
    @Test
    void c_discountFrom25000() {
        assertEquals(7, discount.getDiscountPercent(25_000));
    }

    // d) rot
    @Test
    void d_divideByZero() {
        assertThrows(NullPointerException.class, () -> calculator.divide(1, 0));
    }

    // e) rot
    @Test
    void e_withdrawTooMuch() {
        Account a = new Account(2, "Gates", 100);
        assertTrue(a.withdraw(500));
        assertEquals(100, a.getBalance());
    }

    // f) grün, testet aber nichts
    @Test
    void f_multiply() {
        calculator.multiply(3, 4);
    }

    // g) g1 und g2 hängen voneinander ab, deshalb ist g2 rot. Mach die Tests unabhängig.
    @Test
    @Order(1)
    void g1_deposit() {
        account.deposit(50);
        assertEquals(150, account.getBalance());
    }

    @Test
    @Order(2)
    void g2_newAccountHas100() {
        assertEquals(100, account.getBalance());
    }
}
