# Banken Simulation – Wie funktioniert die Software?

Projekt: [bank/02_bank-vorgabe](bank/02_bank-vorgabe)

## Klassen

- **Bank**
  - Speichert alle Konten in einer `TreeMap` (Kontonummer → Konto)
  - Erstellt Konten: Sparkonto `S-`, Jugendsparkonto `Y-`, Lohnkonto `P-`
  - Kontonummern starten bei 1000 und zählen hoch
  - Einzahlen, Abheben und Auszug drucken immer über die Kontonummer
  - `getBalance()` gibt den Stand der Bank zurück: die Summe aller Konten **negativ** (Geld der Kunden = Schulden der Bank)
  - `printTop5()` / `printBottom5()` sortieren mit den Comparator-Klassen

- **Account** (abstrakt)
  - Kontonummer, Kontostand und Liste von Buchungen
  - `deposit()` und `withdraw()` lehnen negative Beträge ab
  - `canTransact(date)`: Eine Buchung darf nicht vor der letzten Buchung liegen
  - `print()` druckt alle Buchungen, `print(year, month)` nur einen Monat

- **SavingsAccount** (Sparkonto) erbt von Account
  - Kontostand darf nicht unter 0 fallen

- **PromoYouthSavingsAccount** (Jugendsparkonto) erbt von SavingsAccount
  - Bei jeder Einzahlung gibt es 1 % Bonus

- **SalaryAccount** (Lohnkonto) erbt von Account
  - Hat eine Kreditlimite (negative Zahl), bis dahin darf man ins Minus

- **Booking**
  - Eine Buchung mit Datum und Betrag (Abhebungen sind negativ)

- **BankUtils**
  - Formatiert Datum und Beträge für den Kontoauszug

- **AccountBalanceComparator** / **AccountInverseBalanceComparator**
  - Sortieren Konten absteigend bzw. aufsteigend nach Kontostand

## Wichtige Details

- Beträge sind `long` in Millirappen (1 CHF = 100'000), damit es keine Rundungsfehler gibt
- Datum ist eine Zahl: Banktage seit 1.1.1970, jeder Monat hat 30 Tage, jedes Jahr 360 Tage
- `Main` erstellt eine Bank und zwei Konten. Das Lohnkonto mit Limite `12000` wird nicht erstellt, weil die Limite positiv ist

## Zusammenhänge

```
Bank 1 ──── * Account
                 ├── SavingsAccount
                 │      └── PromoYouthSavingsAccount
                 └── SalaryAccount
Account 1 ──── * Booking
Bank ──> AccountBalanceComparator / AccountInverseBalanceComparator
Booking ──> BankUtils
```
