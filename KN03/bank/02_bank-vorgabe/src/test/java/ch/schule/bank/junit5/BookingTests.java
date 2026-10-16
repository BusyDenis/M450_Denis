package ch.schule.bank.junit5;

import ch.schule.BankUtils;
import ch.schule.Booking;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingTests {

    private ByteArrayOutputStream output;
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        originalOut = System.out;
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testInitialization() {
        Booking booking = new Booking(42, 1500);

        assertEquals(42, booking.getDate());
        assertEquals(1500, booking.getAmount());
    }

    @Test
    public void testNegativeAmount() {
        Booking booking = new Booking(1, -500);

        assertEquals(-500, booking.getAmount());
    }

    @Test
    public void testPrint() {
        Booking booking = new Booking(0, 100000);

        booking.print(50000);

        String text = output.toString();
        assertTrue(text.contains("01.01.1970"));
        assertTrue(text.contains(BankUtils.formatAmount(100000)));
        assertTrue(text.contains(BankUtils.formatAmount(150000)));
    }

    @Test
    public void testFormatBankDate() {
        assertEquals("01.01.1970", BankUtils.formatBankDate(0));
        assertEquals("01.02.1970", BankUtils.formatBankDate(30));
        assertEquals("01.01.1971", BankUtils.formatBankDate(360));
    }

    @Test
    public void testFormatAmountHasMinimumLength() {
        assertEquals(10, BankUtils.formatAmount(100000).length());
    }
}
