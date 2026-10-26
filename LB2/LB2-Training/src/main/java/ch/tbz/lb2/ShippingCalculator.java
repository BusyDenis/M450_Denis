package ch.tbz.lb2;

/**
 * Versandkosten:
 * - Bestellwert ab 100 CHF (inklusive): gratis (0 CHF)
 * - sonst nach Gewicht: über 20 kg: 15 CHF, über 5 kg: 9 CHF, sonst 5 CHF
 */
public class ShippingCalculator {

    public double getShippingCost(double orderValue, double weightKg) {
        if (orderValue > 100) {
            return 0;
        }
        if (weightKg > 5) {
            return 9;
        } else if (weightKg > 20) {
            return 15;
        }
        return 5;
    }
}
