package ch.schule.bank.junit5;

import ch.schule.Account;
import ch.schule.BankUtils;
import ch.schule.Booking;
import ch.schule.SalaryAccount;
import ch.schule.SavingsAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTests {

    private Account account;
    private ByteArrayOutputStream output;
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        account = new SavingsAccount("S-1");
        originalOut = System.out;
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testInit() {
        assertEquals("S-1", account.getId());
        assertEquals(0, account.getBalance());
        assertNull(account.getBooking());
    }

    @Test
    public void testDeposit() {
        assertTrue(account.deposit(1, 1000));
        assertTrue(account.deposit(2, 500));
        assertEquals(1500, account.getBalance());
    }

    @Test
    public void testDepositNegativeAmount() {
        assertFalse(account.deposit(1, -100));
        assertEquals(0, account.getBalance());
    }

    @Test
    public void testWithdraw() {
        account.deposit(1, 1000);

        assertTrue(account.withdraw(2, 300));
        assertEquals(700, account.getBalance());
    }

    @Test
    public void testWithdrawNegativeAmount() {
        Account salary = new SalaryAccount("P-1", -1000);

        assertFalse(salary.withdraw(1, -100));
        assertEquals(0, salary.getBalance());
    }

    @Test
    public void testReferences() {
        Account savings = new SavingsAccount("S-2");
        savings.deposit(1, 100);

        assertInstanceOf(Account.class, savings);
        assertFalse(savings.withdraw(2, 200));

        Booking booking = new Booking(1, 100);
        savings.setBooking(booking);
        assertSame(booking, savings.getBooking());
    }

    @Test
    public void testCanTransact() {
        assertTrue(account.canTransact(10));

        account.deposit(10, 100);

        assertTrue(account.canTransact(10));
        assertTrue(account.canTransact(11));
        assertFalse(account.canTransact(9));
        assertFalse(account.deposit(9, 100));
        assertFalse(account.withdraw(9, 50));
    }

    @Test
    public void testPrint() {
        account.deposit(0, 100000);
        account.withdraw(1, 40000);

        account.print();

        String text = output.toString();
        assertTrue(text.contains("Kontoauszug 'S-1'"));
        assertTrue(text.contains("01.01.1970"));
        assertTrue(text.contains("02.01.1970"));
        assertTrue(text.contains(BankUtils.formatAmount(60000)));
    }

    @Test
    public void testMonthlyPrint() {
        account.deposit(5, 1000);
        account.deposit(35, 2000);
        account.deposit(65, 3000);

        account.print(1970, 2);

        String text = output.toString();
        assertTrue(text.contains("Monat: 2.1970"));
        assertTrue(text.contains("06.02.1970"));
        assertFalse(text.contains("06.01.1970"));
        assertFalse(text.contains("06.03.1970"));
    }
}
