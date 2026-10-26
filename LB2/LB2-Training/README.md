# LB2 Training – Praxis (JUnit 5, Mockito, TDD)

Übungen für die LB2 (Modul 450). Der Code ist absichtlich kaputt oder unvollständig.
Ziel: am Schluss ist **alles grün**.

## Starten

- **IntelliJ:** Ordner `LB2-Training` öffnen (als Maven-Projekt), Rechtsklick auf `src/test/java` → *Run 'All Tests'*
- **Terminal:** `mvn test`

## Aufgaben (in dieser Reihenfolge)

| Nr | Datei | Was tun | Thema |
|---|---|---|---|
| A1 | `A1_AnnotationsTest` | Fehlende Annotations + Imports ergänzen | `@Test`, `@BeforeEach`, `@BeforeAll`, `@DisplayName`, `@Disabled` |
| A2 | `A2_FixTheTestsTest` | 7 falsche Tests reparieren (Code ist korrekt) | Assertions, delta, assertThrows, unabhängige Tests |
| A3 | `A3_ParameterizedTest` | Parametrisierte Tests schreiben | `@ParameterizedTest`, `@CsvSource`, `@ValueSource`, Grenzwerte |
| A4 | `A4_MockitoTest` | Service mit Mocks testen | `@Mock`, `@InjectMocks`, `when/thenReturn` (Stub), `verify` (Mock) |
| A5 | `A5_TddPasswordTest` | Tests sind fertig → `PasswordValidator` implementieren | TDD: Red → Green → Refactor |
| A6 | `A6_ShippingTest` | Tests sind korrekt → 2 Fehler in `ShippingCalculator` finden | Bug im Produktivcode, Grenzwerte, if-Reihenfolge |

Ziel am Ende: `Tests run: 46, Failures: 0, Errors: 0, Skipped: 1`

## Lösungen

Im Ordner `loesungen/` (gleiche Ordnerstruktur wie `src/`). Erst selber probieren, dann vergleichen.

## Prüfungs-Tipp

In der LB2 gibt es keine Unterlagen. Übe deshalb die Imports auswendig:

```java
import org.junit.jupiter.api.*;                       // @Test, @BeforeEach, @BeforeAll, @DisplayName, @Disabled
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;     // assertEquals, assertTrue, assertThrows ...
import static org.mockito.Mockito.*;                  // mock, when, verify, never ...
```
