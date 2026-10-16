package ch.schule.bank.junit5;

import ch.schule.PromoYouthSavingsAccount;
import ch.schule.SavingsAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PromoYouthSavingsAccountTests {

    private PromoYouthSavingsAccount account;

    @BeforeEach
    public void setUp() {
        account = new PromoYouthSavingsAccount("Y-1");
    }

    @Test
    public void testDepositAddsOnePercentBonus() {
        assertTrue(account.deposit(1, 10000));
        assertEquals(10100, account.getBalance());
    }

    @Test
    public void testSmallDepositHasNoBonus() {
        account.deposit(1, 99);
        assertEquals(99, account.getBalance());
    }

    @Test
    public void testNegativeDepositIsRejected() {
        assertFalse(account.deposit(1, -10000));
        assertEquals(0, account.getBalance());
    }

    @Test
    public void testWithdrawLikeSavingsAccount() {
        account.deposit(1, 10000);

        assertFalse(account.withdraw(2, 20000));
        assertTrue(account.withdraw(2, 100));
        assertEquals(10000, account.getBalance());
    }

    @Test
    public void testIsSavingsAccount() {
        assertInstanceOf(SavingsAccount.class, account);
    }
}
