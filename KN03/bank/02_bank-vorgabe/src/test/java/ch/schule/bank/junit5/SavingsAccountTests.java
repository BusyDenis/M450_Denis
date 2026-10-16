package ch.schule.bank.junit5;

import ch.schule.SavingsAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SavingsAccountTests {

    private SavingsAccount account;

    @BeforeEach
    public void setUp() {
        account = new SavingsAccount("S-1");
        account.deposit(1, 1000);
    }

    @Test
    public void testWithdrawAllMoney() {
        assertTrue(account.withdraw(2, 1000));
        assertEquals(0, account.getBalance());
    }

    @Test
    public void testWithdrawMoreThanBalance() {
        assertFalse(account.withdraw(2, 1001));
        assertEquals(1000, account.getBalance());
    }

    @Test
    public void testBalanceNeverNegative() {
        account.withdraw(2, 600);
        account.withdraw(3, 600);

        assertEquals(400, account.getBalance());
    }
}
