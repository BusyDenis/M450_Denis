# KN08 – Test Driven Development

## Aufgabe 1 – Setup

- PHP 8.3 installiert (`winget install PHP.PHP.8.3`), in der `php.ini` die Extension `mbstring` aktiviert
- PHPUnit 11 als `phpunit.phar` direkt im Projekt: [FancyApp/phpunit.phar](FancyApp/phpunit.phar)
- In `Test.php` auf das neue PHPUnit umgestellt:
  - `use PHPUnit\Framework\TestCase;` aktiviert
  - `extends PHPUnit_Framework_TestCase` → `extends TestCase`
  - `require_once` mit `__DIR__`, damit die Tests von überall laufen

Tests ausführen:

```bash
cd FancyApp
php phpunit.phar --testdox Testcases/Test.php
```

## Aufgabe 2 – Methoden implementieren

### 1. Red

Vor der Implementierung schlagen alle 3 Tests fehl:

```
Tests: 3, Assertions: 5, Failures: 3.
```

### 2. Green

Code: [FancyApp/Application/MyFancyClass.php](FancyApp/Application/MyFancyClass.php)

| Methode | Was sie macht (abgeleitet aus den Tests) |
|---|---|
| `shortString($text, $length, $ending)` | Kürzt einen Text auf `$length` Zeichen, **inklusive** Endung. Ist der Text kurz genug, kommt er unverändert zurück. Ist die Endung länger als `$length`, gibt es `false`. |
| `calcAverage($values)` | Durchschnitt der Werte. Leere Liste → `0`. Ein Wert ist keine Zahl → `false`. Zahlen als Strings (`'4.5'`) gehen auch. |
| `getOpposite($value, $delimiter)` | Das "Gegenteil": Array → String (`implode`), String → Array (`explode`). Alles andere → `false`. |

```
OK (3 tests, 27 assertions)
 ✔ Short string
 ✔ Calc average
 ✔ Get opposite
```

### 3. Refactor

Der Code ist schon kurz, deshalb nur kleine Punkte:
- Frühe `return`s statt verschachtelte `if`s
- Sprechende Variable `$textLength`

### Stolpersteine

- **Umlaute:** Der Text `'Ich bin Text mit 35 Char Länge (35)'` hat 35 Zeichen, aber 36 Bytes (ä = 2 Bytes). Mit `strlen()` schlägt der Test fehl, deshalb `mb_strlen()` und `mb_substr()`.
- **Reihenfolge:** Zuerst prüfen, ob die Endung zu lang ist, und erst danach, ob der Text schon kurz genug ist. Sonst gibt `shortString('', 20, $endingLong)` einen leeren String statt `false`.
- **Float zu String:** `implode` macht aus `5.0` (Zahl) `"5"`, aus `'5.0'` (String) aber `"5.0"`. Genau das erwartet der Test.

## Aufgabe 3 – Auswerten

Vergleich mit der Lösung einer anderen Gruppe (mit Text-Compare-Tool, z.B. WinMerge oder IntelliJ *Compare Files*):

| Punkt | Unsere Lösung | Andere Gruppe |
|---|---|---|
| Zeichenlänge | `mb_strlen` / `mb_substr` | |
| Prüfung Zahlen | `is_numeric` in einer Schleife | |
| Durchschnitt | `array_sum / count` | |
| getOpposite | `is_array` → `implode`, `is_string` → `explode` | |

**Fazit:** Die Tests beschreiben das Verhalten so genau, dass die Lösungen fast gleich funktionieren müssen. Unterschiede gibt es nur im Stil (Schleife vs. `array_filter`, `if/else` vs. frühes `return`) oder bei Fällen, die die Tests nicht abdecken, z.B. eine negative `$length` oder ein leerer Delimiter. Das zeigt: Was nicht getestet ist, ist auch nicht festgelegt.

## Reflexion TDD

- Die Tests sind die Anforderungen. Man muss nicht raten, was die Methode tun soll.
- Man merkt sofort, wann man fertig ist (alles grün).
- Schwierig: Man muss die Tests genau lesen, z.B. dass die Endung in `$length` mitgezählt wird.
