package ch.tbz.lb2;

/**
 * Versandkosten:
 * - Bestellwert ab 100 CHF (inklusive): gratis (0 CHF)
 * - sonst nach Gewicht: über 20 kg: 15 CHF, über 5 kg: 9 CHF, sonst 5 CHF
 */
// LÖSUNG A6
public class ShippingCalculator {

    public double getShippingCost(double orderValue, double weightKg) {
        // Fehler 1: "ab 100 inklusive" heisst >= und nicht >
        if (orderValue >= 100) {
            return 0;
        }
        // Fehler 2: die grössere Grenze zuerst prüfen, sonst wird "> 20" nie erreicht
        if (weightKg > 20) {
            return 15;
        } else if (weightKg > 5) {
            return 9;
        }
        return 5;
    }
}
