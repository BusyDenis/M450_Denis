# KN05 – Automation Testing

## App starten

```bash
# Backend (Port 8081)
cd app/student-app
mvn spring-boot:run

# Frontend (Port 4200), zweites Terminal
cd app/student-app/src/main/js/my-app
npm install
npm start
```

Hinweis: Spring Boot wurde auf 3.3.5 und Java 21 angehoben, weil das alte Lombok mit Java 21 nicht kompiliert.

---

## Übung 1 – REST-Schnittstelle automatisiert testen

**Tool:** Postman-Collection, automatisch ausgeführt mit **Newman** (Postman auf der Kommandozeile).

Ordner: [rest-tests](rest-tests)

```bash
cd rest-tests
npm install
npm test
```

| Request | Prüfung |
|---|---|
| GET /students | Status 200, Antwort ist eine Liste, "Jonas" ist drin, schneller als 500 ms |
| POST /students | Status 200 |
| GET /students | Liste hat einen Eintrag mehr, neuer Student hat die richtige E-Mail |
| POST /students mit falscher E-Mail | Status 400 (Bonus-Feature) |
| GET /gibtsnicht | Status 404 |

- Resultat: 9 Assertions, 0 Fehler
- HTML-Report: `rest-tests/report/rest-report.html`
- Die Collection kann man auch direkt in Postman importieren und dort mit dem *Collection Runner* starten.

---

## Übung 2 – GUI End-to-End Test

**Tool:** [Playwright](https://playwright.dev). Es öffnet einen echten Chrome-Browser und klickt sich durch die App.

Ordner: [e2e-tests](e2e-tests)

```bash
cd e2e-tests
npm install
npx playwright install chromium
npm test
npm run report
```

| Test | Was passiert |
|---|---|
| Startseite zeigt Logo und Buttons | Logo, "List Students" und "Add Students" sind sichtbar |
| Liste zeigt die Studenten vom Backend | Klick auf "List Students" → Jonas und ann@tbz.ch sind in der Tabelle |
| Submit Button ist ohne Eingabe deaktiviert | Leeres Formular kann nicht abgeschickt werden |
| Neuen Studenten hinzufügen | Name + E-Mail eingeben → Submit → neuer Student steht in der Liste |
| Ungültige E-Mail kann nicht abgeschickt werden | "keine-email" → Fehlermeldung, Button deaktiviert (Bonus-Feature) |

- Resultat: 5 von 5 Tests grün
- Der Browser läuft sichtbar und langsam (`slowMo: 500`), damit man im Video sieht, was passiert
- Pro Test werden Screenshot und Video gespeichert, HTML-Report unter `playwright-report/`

---

## Übung 3 – Lasttest

Siehe [LASTTEST.md](LASTTEST.md)

---

## Bonus – Feature mit Zeitschätzung

Siehe [BONUS_FEATURE.md](BONUS_FEATURE.md)
