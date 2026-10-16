# JUnit 5 – Zusammenfassung

Referenz: [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)

## Annotations

| Annotation | Wofür | Beispiel |
|---|---|---|
| `@Test` | Markiert eine Methode als Test | `@Test void addTwoNumbers() { ... }` |
| `@BeforeEach` | Läuft vor **jedem** Test, z.B. Objekt neu erstellen | `calculator = new Calculator();` |
| `@AfterEach` | Läuft nach **jedem** Test, z.B. aufräumen | `System.setOut(originalOut);` |
| `@BeforeAll` | Läuft **einmal** vor allen Tests (muss `static` sein) | DB-Verbindung öffnen |
| `@AfterAll` | Läuft **einmal** nach allen Tests (muss `static` sein) | DB-Verbindung schliessen |
| `@DisplayName` | Schöner Name im Testreport | `@DisplayName("Zwei Zahlen addieren")` |
| `@Disabled` | Test wird übersprungen | Test für ein Feature, das noch nicht fertig ist |
| `@ParameterizedTest` | Gleicher Test mit vielen Werten | zusammen mit `@CsvSource` oder `@ValueSource` |
| `@Nested` | Tests in Gruppen (innere Klassen) ordnen | alle Tests für "abheben" in einer Gruppe |
| `@Tag` | Tests markieren, um nur bestimmte laufen zu lassen | `@Tag("slow")` |
| `@Timeout` | Test schlägt fehl, wenn er zu lange dauert | `@Timeout(2)` = max. 2 Sekunden |

## Assertions

| Assertion | Prüft | Beispiel |
|---|---|---|
| `assertEquals(expected, actual)` | Werte sind gleich | `assertEquals(5, calculator.add(2, 3));` |
| `assertEquals(expected, actual, delta)` | Kommazahlen mit Toleranz | `assertEquals(0.3, 0.1 + 0.2, 0.0001);` |
| `assertNotEquals` | Werte sind verschieden | |
| `assertTrue` / `assertFalse` | Bedingung ist wahr / falsch | `assertTrue(account.deposit(1, 100));` |
| `assertNull` / `assertNotNull` | Objekt ist null / nicht null | `assertNull(bank.createSalaryAccount(1000));` |
| `assertSame` | Genau dasselbe Objekt | |
| `assertThrows` | Eine Exception wird geworfen | `assertThrows(ArithmeticException.class, () -> calculator.divide(1, 0));` |
| `assertAll` | Mehrere Assertions, alle werden geprüft | `assertAll(() -> assertEquals(...), () -> assertEquals(...));` |
| `assertInstanceOf` | Objekt hat den richtigen Typ | `assertInstanceOf(Account.class, savings);` |
| `fail("...")` | Test schlägt immer fehl | Platzhalter für noch nicht geschriebene Tests |

## Parametrisierte Tests

```java
@ParameterizedTest
@CsvSource({
        "1, 1, 2",
        "10, -5, 5"
})
void addManyValues(double a, double b, double expected) {
    assertEquals(expected, calculator.add(a, b));
}
```

Ein Test, mehrere Durchläufe. Spart viel doppelten Code.

## Tests ausführen

- **IntelliJ:** grüner Pfeil neben der Klasse oder Methode → *Run*. Mit *Run with Coverage* sieht man die Testabdeckung.
- **Maven:** `mvn test` auf der Kommandozeile. Resultate stehen in `target/surefire-reports`.
