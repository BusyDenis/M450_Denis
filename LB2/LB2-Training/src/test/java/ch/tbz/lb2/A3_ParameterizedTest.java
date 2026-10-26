package ch.tbz.lb2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// TODO A3: Parametrisierte Tests für DiscountCalculator
//  Regeln: unter 15'000: 0 %, bis und mit 20'000: 5 %, unter 25'000: 7 %, ab 25'000: 8.5 %,
//          negativer Preis: IllegalArgumentException
//  1. Ersetze discountPercent() durch EINEN @ParameterizedTest mit @CsvSource ("preis, rabatt").
//     Teste vor allem die GRENZWERTE (mind. 6 Zeilen).
//  2. Ersetze negativePriceThrows() durch einen @ParameterizedTest mit @ValueSource (mind. 2 negative Preise).
//  3. Bonus: finalPrice() mit @CsvSource ("preis, endpreis"), z.B. 20000 -> 19000
class A3_ParameterizedTest {

    private final DiscountCalculator discount = new DiscountCalculator();

    @Test
    void discountPercent() {
        fail("TODO A3.1");
    }

    @Test
    void negativePriceThrows() {
        fail("TODO A3.2");
    }

    @Test
    void finalPrice() {
        fail("TODO A3.3 (Bonus)");
    }
}
