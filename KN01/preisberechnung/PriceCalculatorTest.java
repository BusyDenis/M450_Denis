public class PriceCalculatorTest {

    static PriceCalculator calculator;

    public static void main(String[] args) {
        System.out.println("=== Original Code ===");
        calculator = new PriceCalculator();
        printResult(testCalculatePrice());

        System.out.println();
        System.out.println("=== Korrigierter Code ===");
        calculator = new FixedPriceCalculator();
        printResult(testCalculatePrice());
    }

    static boolean testCalculatePrice() {
        double price;
        boolean testOk = true;

        price = calculator.calculatePrice(10000, 0, 0, 0, 0);
        testOk &= check("1: nur Grundpreis", price, 10000);

        price = calculator.calculatePrice(10000, 500, 1000, 2, 10);
        testOk &= check("2: Haendlerrabatt, 2 Extras", price, 10500);

        price = calculator.calculatePrice(10000, 0, 1000, 3, 0);
        testOk &= check("3: 3 Extras = 10% Rabatt", price, 10900);

        price = calculator.calculatePrice(10000, 0, 1000, 5, 0);
        testOk &= check("4: 5 Extras = 15% Rabatt", price, 10850);

        price = calculator.calculatePrice(10000, 0, 1000, 3, 20);
        testOk &= check("5: Haendlerrabatt hoeher als Zubehoerrabatt", price, 8900);

        return testOk;
    }

    static boolean check(String name, double actual, double expected) {
        boolean ok = Math.abs(actual - expected) < 0.01;
        String status = ok ? "OK    " : "FEHLER";
        System.out.println(status + " Test " + name + " -> erwartet " + expected + ", erhalten " + actual);
        return ok;
    }

    static void printResult(boolean allOk) {
        System.out.println(allOk ? "Alle Tests bestanden" : "Es gibt fehlgeschlagene Tests");
    }
}
