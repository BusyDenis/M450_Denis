package ch.schule.bank.junit5;

import ch.schule.SalaryAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SalaryAccountTests {

    private SalaryAccount account;

    @BeforeEach
    public void setUp() {
        account = new SalaryAccount("P-1", -1000);
    }

    @Test
    public void testWithdrawWithinCreditLimit() {
        assertTrue(account.withdraw(1, 800));
        assertEquals(-800, account.getBalance());
    }

    @Test
    public void testWithdrawExactlyToCreditLimit() {
        assertTrue(account.withdraw(1, 1000));
        assertEquals(-1000, account.getBalance());
    }

    @Test
    public void testWithdrawOverCreditLimit() {
        assertFalse(account.withdraw(1, 1001));
        assertEquals(0, account.getBalance());
    }

    @Test
    public void testDepositAndWithdraw() {
        account.deposit(1, 500);

        assertTrue(account.withdraw(2, 1500));
        assertEquals(-1000, account.getBalance());
    }
}
