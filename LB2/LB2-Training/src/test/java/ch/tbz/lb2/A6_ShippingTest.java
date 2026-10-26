package ch.tbz.lb2;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

// TODO A6: Diese Tests sind KORREKT. Die Fehler sind im PRODUKTIVCODE ShippingCalculator (2 Fehler).
//  Finde und behebe sie (ähnlich wie die Preisberechnung in KN01). Die Tests NICHT ändern.
class A6_ShippingTest {

    private final ShippingCalculator shipping = new ShippingCalculator();

    @ParameterizedTest(name = "Bestellwert {0} CHF, {1} kg -> {2} CHF")
    @CsvSource({
            "50, 1, 5",
            "50, 5, 5",
            "50, 5.1, 9",
            "50, 20, 9",
            "50, 20.1, 15",
            "99.95, 30, 15",
            "100, 30, 0",
            "250, 1, 0"
    })
    void shippingCost(double orderValue, double weightKg, double expected) {
        assertEquals(expected, shipping.getShippingCost(orderValue, weightKg));
    }
}
