# KN01 – Lösungen

## Aufgabe 1 – Testformen aus der Praxis

| Testform | Beispiel aus der Praxis | Wie wird getestet? |
|---|---|---|
| Unit-Test | Eine Methode berechnet die MwSt. einer Rechnung | Entwickler schreibt mit JUnit kleine Tests, die die Methode mit festen Werten aufrufen und das Resultat prüfen. Läuft automatisch bei jedem Build. |
| Integrationstest | Backend speichert einen Kunden in der Datenbank | Die API wird aufgerufen und danach wird geprüft, ob der Kunde wirklich in der DB steht. |
| Systemtest / End-to-End | Bestellung in einem Webshop vom Login bis zur Bestätigung | Ein Tool wie Cypress oder Playwright klickt sich automatisch durch den Browser. |
| Abnahmetest (UAT) | Kunde prüft eine neue Funktion vor dem Go-Live | Der Kunde arbeitet eine Checkliste mit Testfällen manuell ab und gibt die Software frei. |
| Lasttest | Wie verhält sich die Seite bei 1000 Benutzern gleichzeitig? | Mit JMeter oder k6 werden viele Anfragen gleichzeitig geschickt und die Antwortzeiten gemessen. |

## Aufgabe 2 – Fehler, Mangel, Schaden

**Fehler:** Ein Online-Shop rechnet bei 3 Artikeln à 10 CHF den Total von 20 CHF aus. Das IST-Verhalten weicht vom SOLL-Verhalten (30 CHF) ab.

**Mangel:** Der Shop rechnet den Preis richtig, zeigt ihn aber als `30.000000001` an oder der Bestellknopf ist auf dem Handy kaum sichtbar. Die Funktion stimmt, die Erwartung wird aber nicht angemessen erfüllt.

**Hoher Schaden:** Die Ariane-5-Rakete (1996) explodierte kurz nach dem Start, weil eine 64-Bit-Zahl in eine 16-Bit-Zahl umgewandelt wurde (Überlauf). Schaden: ca. 370 Millionen Dollar. Ein weiteres Beispiel ist das Strahlentherapie-Gerät Therac-25, wo ein Softwarefehler zu tödlichen Überdosen geführt hat.

## Aufgabe 3 – Testtreiber

Code: [preisberechnung](preisberechnung)

- `PriceCalculator.java` – die Preisberechnung aus der Aufgabe (unverändert übernommen)
- `PriceCalculatorTest.java` – der Testtreiber mit `testCalculatePrice()`

Ausführen:

```bash
cd preisberechnung
javac *.java
java PriceCalculatorTest
```

Getestete Fälle:

| Nr | Grundpreis | Sondermodell | Zubehör | Anzahl Extras | Händlerrabatt | Erwartet |
|---|---|---|---|---|---|---|
| 1 | 10000 | 0 | 0 | 0 | 0 | 10000 |
| 2 | 10000 | 500 | 1000 | 2 | 10 | 10500 |
| 3 | 10000 | 0 | 1000 | 3 | 0 | 10900 |
| 4 | 10000 | 0 | 1000 | 5 | 0 | 10850 |
| 5 | 10000 | 0 | 1000 | 3 | 20 | 8900 |

Test 2, 4 und 5 schlagen fehl. Damit zeigt der Testtreiber die Fehler im Code.

## Aufgabe 3 – Bonus: Der Fehler

1. **Reihenfolge der if-Abfragen:** `extras >= 3` wird vor `extras >= 5` geprüft. Bei 5 Extras ist schon die erste Bedingung wahr, deshalb gibt es nie 15 % Rabatt.
2. **Händlerrabatt wird auf das Zubehör angewendet:** `if (discount > addon_discount) addon_discount = discount;` gehört nicht hierhin. Der Händlerrabatt gilt laut Aufgabe nur für den Grundpreis.

Korrigiert:

```java
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
```

Mit der korrigierten Version (`FixedPriceCalculator.java`) laufen alle Tests durch.
