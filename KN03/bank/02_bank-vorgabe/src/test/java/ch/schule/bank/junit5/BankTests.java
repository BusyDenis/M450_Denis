package ch.schule.bank.junit5;

import ch.schule.Bank;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class BankTests {

    private Bank bank;
    private ByteArrayOutputStream output;
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        bank = new Bank();
        originalOut = System.out;
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testCreate() {
        assertEquals("S-1000", bank.createSavingsAccount());
        assertEquals("Y-1001", bank.createPromoYouthSavingsAccount());
        assertEquals("P-1002", bank.createSalaryAccount(-5000));
    }

    @Test
    public void testCreateSalaryAccountWithPositiveLimit() {
        assertNull(bank.createSalaryAccount(1000));
    }

    @Test
    public void testDeposit() {
        String id = bank.createSavingsAccount();

        assertTrue(bank.deposit(id, 1, 1000));
        assertEquals(1000, bank.getBalance(id));
    }

    @Test
    public void testDepositUnknownAccount() {
        assertFalse(bank.deposit("X-999", 1, 1000));
    }

    @Test
    public void testWithdraw() {
        String id = bank.createSavingsAccount();
        bank.deposit(id, 1, 1000);

        assertTrue(bank.withdraw(id, 2, 400));
        assertEquals(600, bank.getBalance(id));
    }

    @Test
    public void testWithdrawUnknownAccount() {
        assertFalse(bank.withdraw("X-999", 1, 1000));
    }

    @Test
    public void testBalanceUnknownAccount() {
        assertEquals(0, bank.getBalance("X-999"));
    }

    @Test
    public void testPrint() {
        String id = bank.createSavingsAccount();
        bank.deposit(id, 1, 1000);

        bank.print(id);
        bank.print("X-999");

        String text = output.toString();
        assertTrue(text.contains("Kontoauszug 'S-1000'"));
        assertFalse(text.contains("X-999"));
    }

    @Test
    public void testMonthlyPrint() {
        String id = bank.createSavingsAccount();
        bank.deposit(id, 35, 1000);

        bank.print(id, 1970, 2);
        bank.print("X-999", 1970, 2);

        String text = output.toString();
        assertTrue(text.contains("Kontoauszug 'S-1000' Monat: 2.1970"));
        assertTrue(text.contains("06.02.1970"));
        assertFalse(text.contains("X-999"));
    }

    @Test
    public void testBalance() {
        String first = bank.createSavingsAccount();
        String second = bank.createSavingsAccount();
        bank.deposit(first, 1, 1000);
        bank.deposit(second, 1, 500);

        assertEquals(-1500, bank.getBalance());
    }

    @Test
    public void testTop5() {
        for (int i = 1; i <= 6; i++) {
            String id = bank.createSavingsAccount();
            bank.deposit(id, 1, i * 100);
        }

        bank.printTop5();

        String[] lines = output.toString().trim().split("\\R");
        assertEquals(5, lines.length);
        assertEquals("S-1005: 600", lines[0]);
        assertEquals("S-1001: 200", lines[4]);
    }

    @Test
    public void testBottom5() {
        for (int i = 1; i <= 6; i++) {
            String id = bank.createSavingsAccount();
            bank.deposit(id, 1, i * 100);
        }

        bank.printBottom5();

        String[] lines = output.toString().trim().split("\\R");
        assertEquals(5, lines.length);
        assertEquals("S-1000: 100", lines[0]);
        assertEquals("S-1004: 500", lines[4]);
    }

    @Test
    public void testSameBalance() {
        String first = bank.createSavingsAccount();
        String second = bank.createSavingsAccount();
        bank.deposit(first, 1, 100);
        bank.deposit(second, 1, 100);

        bank.printTop5();
        bank.printBottom5();

        assertTrue(output.toString().contains("S-1000: 100"));
        assertTrue(output.toString().contains("S-1001: 100"));
    }

    @Test
    public void testAccountReference() {
        assertNull(bank.getAccount());
    }
}
