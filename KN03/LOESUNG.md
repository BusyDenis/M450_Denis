# KN03 – Lösungen

> KI-Nutzung: Lösung mit Hilfe eines KI-Agenten (Claude Code) erstellt, mit Erlaubnis der Lehrperson.

## Unit Testing

### Aufgabe 1 – Simpler Rechner

Projekt: [calculator](calculator)

- `src/main/java/.../Calculator.java` – add, subtract, multiply, divide
- `src/test/java/.../CalculatorTest.java` – 14 Tests mit `@BeforeEach`, `@Test`, `@DisplayName`, `@ParameterizedTest`, `assertThrows`

Ausführen:
1. IntelliJ: Rechtsklick auf `CalculatorTest` → *Run*
2. Kommandozeile: `cd calculator` und `mvn test`

```
$ mvn test
Running ch.tbz.calculator.CalculatorTest
Tests run: 14, Failures: 0, Errors: 0, Skipped: 0 - ch.tbz.calculator.CalculatorTest
Tests run: 14, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### Aufgabe 2 – JUnit Zusammenfassung

Siehe [JUNIT_ZUSAMMENFASSUNG.md](JUNIT_ZUSAMMENFASSUNG.md)

### Aufgabe 3 – Banken Simulation

Siehe [BANK_DOKU.md](BANK_DOKU.md)

### Aufgabe 4 – Unit-Tests Bank

Projekt: [bank/02_bank-vorgabe](bank/02_bank-vorgabe)

```bash
cd bank/02_bank-vorgabe
mvn test
```

- 40 Tests, alle grün
- Coverage-Report (JaCoCo): `target/site/jacoco/index.html`

```
$ mvn test
Running ch.schule.bank.junit5.AccountTests
Tests run: 9, Failures: 0, Errors: 0, Skipped: 0 - ch.schule.bank.junit5.AccountTests
Running ch.schule.bank.junit5.BankTests
Tests run: 14, Failures: 0, Errors: 0, Skipped: 0 - ch.schule.bank.junit5.BankTests
Running ch.schule.bank.junit5.BookingTests
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0 - ch.schule.bank.junit5.BookingTests
Running ch.schule.bank.junit5.PromoYouthSavingsAccountTests
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0 - ch.schule.bank.junit5.PromoYouthSavingsAccountTests
Running ch.schule.bank.junit5.SalaryAccountTests
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0 - ch.schule.bank.junit5.SalaryAccountTests
Running ch.schule.bank.junit5.SavingsAccountTests
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0 - ch.schule.bank.junit5.SavingsAccountTests
Tests run: 40, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

![Bank Coverage](screenshots/bank-coverage.png)

| Klasse | Line Coverage |
|---|---|
| Account | 100 % |
| Bank | 97 % |
| Booking | 100 % |
| SavingsAccount | 100 % |
| SalaryAccount | 100 % |
| PromoYouthSavingsAccount | 100 % |
| BankUtils | 93 % |
| Comparators | 100 % |
| Main | 0 % (nur Startklasse) |

Bei `print()` wird `System.out` in einen `ByteArrayOutputStream` umgeleitet. So kann man prüfen, was gedruckt wurde.
