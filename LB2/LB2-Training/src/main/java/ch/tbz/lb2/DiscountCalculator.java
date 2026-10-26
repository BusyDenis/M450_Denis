package ch.tbz.lb2;

/**
 * Rabattregeln Autohaus (wie KN02):
 * unter 15'000 CHF: 0 %, bis und mit 20'000 CHF: 5 %, unter 25'000 CHF: 7 %, ab 25'000 CHF: 8.5 %.
 * Negativer Preis: IllegalArgumentException.
 */
public class DiscountCalculator {

    public double getDiscountPercent(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Preis darf nicht negativ sein");
        }
        if (price < 15_000) {
            return 0;
        } else if (price <= 20_000) {
            return 5;
        } else if (price < 25_000) {
            return 7;
        }
        return 8.5;
    }

    public double getFinalPrice(double price) {
        return price * (100 - getDiscountPercent(price)) / 100;
    }
}
