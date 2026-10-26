package ch.tbz.lb2;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// LÖSUNG A3
class A3_ParameterizedTest {

    private final DiscountCalculator discount = new DiscountCalculator();

    // 8 Zeilen = 8 Durchläufe. Pro Zeile: 1. Wert -> price, 2. Wert -> expected
    @ParameterizedTest(name = "{0} CHF -> {1} %")
    @CsvSource({
            "0, 0",
            "14999.95, 0",     // knapp unter der Grenze
            "15000, 5",        // genau auf der Grenze
            "20000, 5",        // "bis und mit" 20'000
            "20000.05, 7",
            "24999.95, 7",
            "25000, 8.5",
            "100000, 8.5"
    })
    void discountPercent(double price, double expected) {
        assertEquals(expected, discount.getDiscountPercent(price));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.05, -1, -15000})
    void negativePriceThrows(double price) {
        assertThrows(IllegalArgumentException.class, () -> discount.getDiscountPercent(price));
    }

    @ParameterizedTest
    @CsvSource({
            "10000, 10000",
            "20000, 19000",
            "24000, 22320",
            "25000, 22875"
    })
    void finalPrice(double price, double expected) {
        assertEquals(expected, discount.getFinalPrice(price), 0.001);
    }
}
