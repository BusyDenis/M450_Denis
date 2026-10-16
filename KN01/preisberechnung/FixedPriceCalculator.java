public class FixedPriceCalculator extends PriceCalculator {

    @Override
    double calculatePrice(double baseprice, double specialprice, double extraprice, int extras, double discount) {
        double addonDiscount;

        if (extras >= 5) {
            addonDiscount = 15;
        } else if (extras >= 3) {
            addonDiscount = 10;
        } else {
            addonDiscount = 0;
        }

        return baseprice / 100.0 * (100 - discount)
                + specialprice
                + extraprice / 100.0 * (100 - addonDiscount);
    }
}
